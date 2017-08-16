package com.softserve.academy.spaced.repetition.logger;

import com.softserve.academy.spaced.repetition.logger.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogException {
    @Autowired
    Logger logger;

    @AfterThrowing(value = "execution(* com.softserve.academy.spaced.repetition.controller.*.*(..))", throwing = "ex")
    public void handlingController(JoinPoint joinPoint, Exception ex) {
        System.out.println(logger);
        logger.log("Exception in class = " + joinPoint.getThis() + ", method = " + joinPoint.getSignature());
    }

}
