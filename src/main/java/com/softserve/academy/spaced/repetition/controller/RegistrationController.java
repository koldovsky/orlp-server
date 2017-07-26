package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountVerificationByEmailService verificationService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity <?> addUser(@RequestBody User userFromClient) {
        return registrationService.registerNewUser(userFromClient);
    }

//    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
//    public ResponseEntity confirmRegistration
//            (@RequestParam("token") String token) {
//        return verificationService.accountVerification(token);
//    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.POST)
    public ResponseEntity confirmRegistration
            (@RequestBody String token) {
        return verificationService.accountVerification(token);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String test() {
        registrationService.sendConfirmationEmailMessage(userRepository.findUserByAccount_Email("dsaw@sda"));
        return "page";
    }
}