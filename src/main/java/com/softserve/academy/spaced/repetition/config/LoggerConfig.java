package com.softserve.academy.spaced.repetition.config;

import com.softserve.academy.spaced.repetition.logger.Log4jImpl;
import com.softserve.academy.spaced.repetition.logger.Logger;
import com.softserve.academy.spaced.repetition.logger.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
    private static final Logger CURRENTLOGGER = new Log4jImpl();

    public Logger configLogger() {
        LoggerFactory.register(CURRENTLOGGER);
        return LoggerFactory.getLogger();
    }
}
