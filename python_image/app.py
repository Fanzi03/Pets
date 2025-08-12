from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import torch
from diffusers import StableDiffusionPipeline
from io import BytesIO
import logging
from typing import Optional
import os
import uuid
from minio import Minio
import json
from minio.error import S3Error

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(title="Stable Diffusion API", version="1.0.0")

# Global variables
pipeline: Optional[StableDiffusionPipeline] = None
minio_client: Optional[Minio] = None

class GenerateRequest(BaseModel):
    prompt: str
    width: int = 512
    height: int = 512
    num_inference_steps: int = 20
    guidance_scale: float = 7.5

@app.on_event("startup")
async def load_model():
    """Load model and setup MinIO on startup"""
    global pipeline, minio_client
    
    try:
        # Setup MinIO
        logger.info("🗄️ Setting up MinIO...")
        minio_client = Minio(
            "minio:9000",
            access_key="minioadmin",
            secret_key="minioadmin",
            secure=False
        )
        
        # Create bucket if doesn't exist
        bucket_name = "ai-images"
        if not minio_client.bucket_exists(bucket_name):
            minio_client.make_bucket(bucket_name)
            logger.info(f"✅ Created bucket: {bucket_name}")
            
        public_policy = {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Effect": "Allow",
                    "Principal": {"AWS": ["*"]},
                    "Action": ["s3:GetObject"],
                    "Resource": [f"arn:aws:s3:::{bucket_name}/*"]
                }
            ]
        }
        try:
            minio_client.set_bucket_policy(bucket_name, json.dumps(public_policy))
            logger.info(f"🌍 Public policy set for {bucket_name}")
        except S3Error as e:
            logger.warning(f"⚠️ Failed to set public policy: {e}")
        
        logger.info("🚀 Loading Stable Diffusion model...")
        
        # Check device availability
        if torch.cuda.is_available():
            device = "cuda"
            torch_dtype = torch.float16
            logger.info("✅ Using CUDA")
            logger.info(f"GPU: {torch.cuda.get_device_name(0)}")
        else:
            device = "cpu"
            torch_dtype = torch.float32
            logger.info("⚠️ Using CPU (will be slow)")
        
        # Model path from environment variable or default
        model_id = os.getenv("HF_MODEL_REPO", "runwayml/stable-diffusion-v1-5")
        model_path = os.getenv("MODEL_PATH", None)
        
        logger.info(f"📂 Model: {model_path if model_path else model_id}")
        
        # Load model
        if model_path and os.path.exists(model_path) and os.path.exists(os.path.join(model_path, "model_index.json")):
            pipeline = StableDiffusionPipeline.from_pretrained(
                model_path,
                torch_dtype=torch_dtype,
                use_safetensors=True,
                local_files_only=True
            )
            logger.info("📁 Loaded local model")
        else:
            logger.info(f"📥 Loading model from HuggingFace: {model_id}")
            pipeline = StableDiffusionPipeline.from_pretrained(
                model_id,
                torch_dtype=torch_dtype,
                use_safetensors=True
            )
            logger.info("🌐 Loaded model from HuggingFace")
        
        # Move to device
        pipeline = pipeline.to(device)
        
        # Memory optimizations
        if device == "cuda":
            pipeline.enable_attention_slicing()
            try:
                pipeline.enable_xformers_memory_efficient_attention()
                logger.info("✨ Enabled xformers optimization")
            except Exception as e:
                logger.warning(f"⚠️ xformers not available: {e}")
        else:
            pipeline.enable_attention_slicing()
        
        logger.info("🎉 Model successfully loaded and ready!")
        
        # Test generation for warmup
        logger.info("🔥 Warming up model...")
        with torch.no_grad():
            # Fixed syntax error
            result = pipeline("test", num_inference_steps=1, guidance_scale=1.0, width=64, height=64)
        logger.info("✅ Model warmed up!")
        
    except Exception as e:
        logger.error(f"❌ Model loading error: {e}")
        raise e

@app.get("/health")
async def health_check():
    """Service readiness check"""
    if pipeline is None:
        raise HTTPException(status_code=503, detail="Model is still loading")
    
    return {
        "status": "healthy",
        "model_loaded": True,
        "device": "cuda" if torch.cuda.is_available() else "cpu",
        "model": os.getenv("HF_MODEL_REPO", "runwayml/stable-diffusion-v1-5"),
        "torch_version": torch.__version__,
        "cuda_available": torch.cuda.is_available()
    }

@app.post("/generate")
async def generate_image(request: GenerateRequest):
    """Generate image and save to MinIO - returns URL"""
    if pipeline is None:
        raise HTTPException(status_code=503, detail="Model not loaded")
    
    try:
        logger.info(f"🎨 Generating: '{request.prompt}' ({request.width}x{request.height})")
        
        # Parameter validation
        if request.width > 1024 or request.height > 1024:
            raise HTTPException(status_code=400, detail="Maximum resolution is 1024x1024")
        
        if request.num_inference_steps > 50:
            raise HTTPException(status_code=400, detail="Maximum 50 steps")
        
        # Generate image
        device = next(pipeline.unet.parameters()).device
        
        with torch.autocast(device.type if device.type != "cpu" else "cpu"):
            result = pipeline(
                prompt=request.prompt,
                width=request.width,
                height=request.height,
                num_inference_steps=request.num_inference_steps,
                guidance_scale=request.guidance_scale,
                generator=torch.Generator(device=device).manual_seed(42)
            )
        
        image = result.images[0]
        
        # Convert to bytes for MinIO
        buffer = BytesIO()
        image.save(buffer, format="PNG", optimize=True)
        image_bytes = buffer.getvalue()
        
        # Generate unique filename
        filename = f"generated_{uuid.uuid4().hex[:8]}.png"
        
        # Upload to MinIO
        minio_client.put_object(
            "ai-images",
            filename,
            BytesIO(image_bytes),
            length=len(image_bytes),
            content_type="image/png"
        )
        
        # Form public URL
        public_host = "http://localhost:9000".rstrip("/")
        image_url = f"{public_host}/ai-images/{filename}" 
        
        logger.info(f"✅ Image ready! URL: {image_url}")
        
        return {
            "url": image_url,
            "prompt": request.prompt,
            "width": request.width,
            "height": request.height,
            "steps": request.num_inference_steps,
            "guidance": request.guidance_scale
        }
        
    except Exception as e:
        logger.error(f"❌ Generation error: {e}")
        raise HTTPException(status_code=500, detail=f"Generation error: {str(e)}")

@app.get("/")
async def root():
    """API information"""
    return {
        "service": "Stable Diffusion API",
        "version": "1.0.0",
        "model": os.getenv("HF_MODEL_REPO", "runwayml/stable-diffusion-v1-5"),
        "endpoints": {
            "health": "GET /health - Readiness check",
            "generate": "POST /generate - Image generation",
            "docs": "GET /docs - Swagger documentation"
        },
        "status": "ready" if pipeline else "loading"
    }

@app.get("/status")
async def get_status():
    """Detailed service status"""
    return {
        "model_loaded": pipeline is not None,
        "cuda_available": torch.cuda.is_available(),
        "device": "cuda" if torch.cuda.is_available() else "cpu",
        "torch_version": torch.__version__,
        "model_path": os.getenv("MODEL_PATH", "HuggingFace"),
        "model_repo": os.getenv("HF_MODEL_REPO", "runwayml/stable-diffusion-v1-5")
    }

# Fixed syntax error
if __name__ == "__main__":
    import uvicorn
    logger.info("🚀 Starting Stable Diffusion API...")
    uvicorn.run(app, host="0.0.0.0", port=8000, log_level="info")
