package com.softserve.academy.spaced.repetition.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TestController {

    @RequestMapping("/private/test")
    public String test() {
        return "test";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public ResponseEntity test2(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "test=test");
        return new ResponseEntity(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public ResponseEntity test3(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "test2=qddd");
        headers.add("test", "test2");
        return new ResponseEntity(headers, HttpStatus.OK);
    }
}
