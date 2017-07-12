package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.domain.OnRegistrationCompleteEvent;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity <?> addUser(@RequestBody User userFromClient, WebRequest request) {
        User createdUser = registrationService.validateAndCreateUser(userFromClient).getBody();
        String URL = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(createdUser, request.getLocale(), URL));


    }
}





