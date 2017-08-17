package com.softserve.academy.spaced.repetition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class TestController {


    @GetMapping("/api/test")
    public String test() throws SQLException {
        throw new SQLException();

    }
}
