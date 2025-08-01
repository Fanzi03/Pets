package org.example.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImagePetKafkaConsumer {
   
    @KafkaListener
    public void imagePetConsume(String imageUrl){
        
        log.info("url", imageUrl);
    }
}
