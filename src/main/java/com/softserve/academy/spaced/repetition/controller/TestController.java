package com.softserve.academy.spaced.repetition.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jarki on 7/6/2017.
 */
@RestController
public class TestController {

//    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/test")
    public String test(){
        return "test";
    }


}
