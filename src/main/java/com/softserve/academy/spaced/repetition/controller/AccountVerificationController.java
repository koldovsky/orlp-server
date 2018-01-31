package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountVerificationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountVerificationController.class);
    @Autowired
    private AccountVerificationByEmailService verificationService;

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.POST)
    public ResponseEntity confirmRegistration(@RequestBody String token) {
        verificationService.accountVerification(token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "api/verification/token")
    public ResponseEntity<String> verificationToken(@RequestBody String token) {
        LOGGER.debug("Token verification");
        String emailFromToken = verificationService.getAccountEmail(token);
        return ResponseEntity.ok(emailFromToken);
    }
}
