package com.week5.SecurityApp.SecurityApplication.aspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class LoggingAspect {

//    @Before("execution(* getAllPost(..))")
    @Before("execution(* com/week5/SecurityApp/SecurityApplication/services/PostServiceImpl.java(..))")
    public void beforePostServiceMethods(JoinPoint joinPoint){
        log.info("Before method call: {}", joinPoint.getSignature());
    }

}
