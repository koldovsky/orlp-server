package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingActionType;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.BlankFieldException;
import com.softserve.academy.spaced.repetition.exceptions.EmailDoesntExistException;
import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
import com.softserve.academy.spaced.repetition.exceptions.ExpiredTokenForVerificationException;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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
    private UserRepository userRepository;
    @Autowired
    private AccountVerificationByEmailService verificationService;

    @Auditable(actionType = AuditingActionType.SIGN_UP)
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity <Person> addUser(@RequestBody User userFromClient) throws BlankFieldException, MailException, EmailUniquesException {
        User user = registrationService.registerNewUser(userFromClient);
        registrationService.sendConfirmationEmailMessage(user);
        return new ResponseEntity <Person>(user.getPerson(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.POST)
    public ResponseEntity confirmRegistration
            (@RequestBody String token) throws EmailDoesntExistException, ExpiredTokenForVerificationException {
        verificationService.accountVerification(token);
        return new ResponseEntity(HttpStatus.OK);
    }
}
