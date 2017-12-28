package com.softserve.academy.spaced.repetition.utils.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Aspect
@Component
public class LogExceptionAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogExceptionAspect.class);

    @AfterThrowing(value = "execution(* com.softserve.academy.spaced.repetition.controller.*.*(..))", throwing = "ex")
    public void loggingExceptionsFromControllers(JoinPoint joinPoint, Exception ex) {
        LOGGER.error("Exception in controller = {} method = {}", joinPoint.getThis(), joinPoint.getSignature(), ex);
    }

    @AfterThrowing(value = "execution(* com.softserve.academy.spaced.repetition.service.*.*(..))", throwing = "ex")
    public void loggingExceptionsFromServices(JoinPoint joinPoint, Exception ex) {
        LOGGER.error("Exception in service = {} method = {}", joinPoint.getThis(), joinPoint.getSignature(), ex);
    }

}