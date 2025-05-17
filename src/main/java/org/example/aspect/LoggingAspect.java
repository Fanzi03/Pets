package org.example.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * org.example.controller.*.*(..))")
    public void controllerLog(){}

    @Pointcut("execution(public * org.example.service.*.*(..))")
    public void serviceLog(){}

    @Before("controllerLog()")
    public void beforeController(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if(attributes != null){
            request = attributes.getRequest();
        }
        if(request != null){
            log.info("NEW REQUEST: IP: {}, URL: {}, HTTP_METHOD: {}, CONTROLLER_METHOD: {}.{}",
                request.getRemoteAddr(),
                request.getRequestURL().toString(),
                    request.getMethod(),
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        }
    }

    @Before("serviceLog()")
    public void beforeService(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        Object[] args = joinPoint.getArgs();

        String argsString = args.length > 0 ? Arrays.toString(args) : "METHOD HAS NO ARGUMENTS";

        log.info("RUN SERVICE: SERVICE_METHOD {}.{}\n METHOD ARGUMENTS: [{}]",
            className, methodName, argsString);
    }

    @AfterReturning(returning = "returnObject", pointcut = "controllerLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnObject){
        log.info("Controller Method executed successfully: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("RETURN VALUE: {}", returnObject);
    }

    @Around("controllerLog()")
    public Object logExecutionTime(ProceedingJoinPoint pjs) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = pjs.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Execution method: {}.{}. Execution time: {}",
                pjs.getSignature().getDeclaringTypeName(), pjs.getSignature().getName(), executionTime);
        return proceed;
    }

    @AfterThrowing(throwing = "ex", pointcut = "controllerLog()")
    public void throwsException(JoinPoint js, Exception ex){
        String methodName = js.getSignature().getName();
        String className  = js.getSignature().getDeclaringTypeName();

        log.error("Exception in {}.{} with args {}",
                className, methodName, Arrays.toString(js.getArgs()), ex);
    }

}
