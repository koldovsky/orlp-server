package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RegistrationService registrationService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity <?> addUser(@RequestBody User userFromClient) {

        return registrationService.validateAndCreateUser(userFromClient);


    }
}





