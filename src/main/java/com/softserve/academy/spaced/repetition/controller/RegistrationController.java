package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.NewAccountPasswordDTO;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api")
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
    @PostMapping(value = "/registration")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity<Person> addUser(@Validated(Request.class) @RequestBody User userFromClient) {
        User user = registrationService.registerNewUser(userFromClient);
        registrationService.sendConfirmationEmailMessage(user);
        return new ResponseEntity<>(user.getPerson(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/registration-confirm")
    public ResponseEntity confirmRegistration(@RequestBody String token) {
        verificationService.accountVerification(token);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/verification/token")
    public ResponseEntity<String> verificationToken(@RequestBody String token) {
        LOGGER.debug("Token verification");
        String emailFromToken = verificationService.getAccountEmail(token);
        return ResponseEntity.ok(emailFromToken);
    }

    @GetMapping("/confirmation-mail")
    public ResponseEntity sendConfirmationMail() throws NotAuthorisedUserException {
        userService.activateAccount();
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/reset/password")
    public ResponseEntity<String> sendResetPasswordMail(@RequestBody String email) {
        LOGGER.debug("Send reset password mail to email: {}", email);
        String accountStatus = accountService.checkAccountStatusAndSendMail(email);
        return ResponseEntity.ok(accountStatus);
    }

    @PutMapping(value = "/create/password")
    public ResponseEntity createNewPasswordForUser(@Validated(Request.class) @RequestBody
                                                           NewAccountPasswordDTO newAccountPasswordDTO) {
        LOGGER.debug("Created new password for: {}", newAccountPasswordDTO.getEmail());
        accountService.createNewAccountPassword(newAccountPasswordDTO.getEmail(), newAccountPasswordDTO.getPassword());
        return ResponseEntity.ok().build();
    }
}
