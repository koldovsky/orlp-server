package com.softserve.academy.spaced.repetition.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogException {
    @Autowired
    Logger logger;

    @AfterThrowing(value = "execution(* com.softserve.academy.spaced.repetition.controller.*.*(..))", throwing = "ex")
    public void loggingExceptionsFromControllers(JoinPoint joinPoint, Exception ex) {
        System.out.println(logger);
        logger.log("Exception in controller = " + joinPoint.getThis() + ", method = " + joinPoint.getSignature());
    }

    @AfterThrowing(value = "execution(* com.softserve.academy.spaced.repetition.service.*.*(..))", throwing = "ex")
    public void loggingExceptionsFromServices(JoinPoint joinPoint, Exception ex) {
        System.out.println(logger);
        logger.log("Exception in service = " + joinPoint.getThis() + ", method = " + joinPoint.getSignature());
    }

}
