package com.softserve.academy.spaced.repetition.service.cacheScheduler;

import com.softserve.academy.spaced.repetition.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CacheScheduler.class);

    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Started adding categories to cache...");
        categoryService.getTopCategory();
        categoryService.getAllCategory();
        logger.info("Finished adding categories to cache");
    }

}