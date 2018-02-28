package com.softserve.academy.spaced.repetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
@EnableCaching
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
