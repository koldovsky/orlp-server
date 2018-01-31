package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    private AccountVerificationByEmailService verificationService;

    @Auditable(action = AuditingAction.SIGN_UP)
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Person> addUser(@Validated(Request.class) @RequestBody User userFromClient) {
        User user = registrationService.registerNewUser(userFromClient);
        registrationService.sendConfirmationEmailMessage(user);
        return new ResponseEntity<>(user.getPerson(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.POST)
    public ResponseEntity confirmRegistration(@RequestBody String token) {
        verificationService.accountVerification(token);
        return new ResponseEntity(HttpStatus.OK);
    }
}
