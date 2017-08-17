package com.softserve.academy.spaced.repetition.logger;

import org.springframework.stereotype.Component;

@Component
public abstract class LoggerFactory {
    private static Logger logger;

    public static void register(Logger newLogger) {
        logger = newLogger;
    }

    public static Logger getLogger() {
        return logger;
    }


}
