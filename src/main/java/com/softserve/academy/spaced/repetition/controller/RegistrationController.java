package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.chrono.Chronology;
import java.util.Calendar;

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


//        String email = userFromClient.getAccount().getEmail();
//        boolean isValidEmail = false;
//        isValidEmail = registrationService.validateEmail(email);

        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);

        if (userService.findUserByEmail(userFromClient.getAccount().getEmail()) != null) {
            System.out.println(userService.findUserByEmail(userFromClient.getAccount().getEmail()));
            return new ResponseEntity("email is already in use", textPlainHeaders, HttpStatus.BAD_REQUEST);
        } else {

            userFromClient.getAccount().setLastPasswordResetDate(Calendar.getInstance().getTime());
            userService.addUser(userFromClient);
            Chronology result;
            return new ResponseEntity <User>(userFromClient, HttpStatus.CREATED);
        }
    }

}





