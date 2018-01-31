package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.NewAccountPasswordDTO;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class RegistrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);


    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private AccountVerificationByEmailService verificationService;
    @Autowired
    UserService userService;
    @Autowired
    private AccountService accountService;

    @Auditable(action = AuditingAction.SIGN_UP)
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Person> addUser(@Validated(Request.class) @RequestBody User userFromClient) {
        User user = registrationService.registerNewUser(userFromClient);
        registrationService.sendConfirmationEmailMessage(user);
        return new ResponseEntity<>(user.getPerson(), HttpStatus.CREATED);
    }
}
