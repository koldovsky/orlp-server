package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    AccountVerificationByEmailService verificationService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity addUser(@RequestBody User userFromClient) {
        return registrationService.registerNewUser(userFromClient);
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.POST)
    public ResponseEntity confirmRegistration(@RequestBody String token) {
        return verificationService.accountVerification(token);
    }
}