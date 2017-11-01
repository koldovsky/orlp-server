package com.softserve.academy.spaced.repetition.logger;

import com.softserve.academy.spaced.repetition.Application;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Aspect
@Component
public class LogException {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @AfterThrowing(value = "execution(* com.softserve.academy.spaced.repetition.controller.*.*(..))", throwing = "ex")
    public void loggingExceptionsFromControllers(JoinPoint joinPoint, Exception ex) {
        logger.trace("Exception in controller = " + joinPoint.getThis() + ", method = " + joinPoint.getSignature());
    }

    @AfterThrowing(value = "execution(* com.softserve.academy.spaced.repetition.service.*.*(..))", throwing = "ex")
    public void loggingExceptionsFromServices(JoinPoint joinPoint, Exception ex) {
        logger.trace("Exception in service = " + joinPoint.getThis() + ", method = " + joinPoint.getSignature());
    }

}