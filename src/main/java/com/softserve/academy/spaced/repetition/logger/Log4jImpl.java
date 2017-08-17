package com.softserve.academy.spaced.repetition.logger;

import org.springframework.stereotype.Component;

@Component
public class Log4jImpl implements Logger {
    org.apache.log4j.Logger logger;

    public Log4jImpl() {
        logger = org.apache.log4j.Logger.getLogger(this.getClass());
    }

    @Override
    public void log(String message) {
        logger.warn(message);
    }
}
