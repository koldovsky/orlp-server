package com.softserve.academy.spaced.repetition.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TestController {

    @RequestMapping("/private/test")
    public String test() {
        return "test";
    }
}
