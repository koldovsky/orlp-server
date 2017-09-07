package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.BlankFieldException;
import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
import com.softserve.academy.spaced.repetition.exceptions.ObjectHasNullFieldsException;
import com.softserve.academy.spaced.repetition.service.validators.AbstractValidator;
import com.softserve.academy.spaced.repetition.service.validators.BlankFieldValidator;
import com.softserve.academy.spaced.repetition.service.validators.EmailUniquesValidator;
import com.softserve.academy.spaced.repetition.service.validators.NullFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private EmailUniquesValidator emailUniquesValidator;
    @Autowired
    private BlankFieldValidator blankFieldValidator;
    @Autowired
    private NullFieldsValidator nullFieldsValidator;

    public User registerNewUser(User user) throws BlankFieldException, EmailUniquesException, ObjectHasNullFieldsException {
        AbstractValidator validator = getChainOfValidators();
        validator.doValidate(user);
        return createNewUser(user);
    }

    private AbstractValidator getChainOfValidators() {
        nullFieldsValidator.setNextValidator(blankFieldValidator);
        blankFieldValidator.setNextValidator(emailUniquesValidator);
        return nullFieldsValidator;
    }

    public User createNewUser(User user) {
        Set <Authority> listOfAuthorities = new HashSet <>();
        listOfAuthorities.add(new Authority(AuthorityName.ROLE_USER));
        user.getAccount().setLastPasswordResetDate(Calendar.getInstance().getTime());
        user.setFolder(new Folder());
        user.getAccount().setStatus(AccountStatus.INACTIVE);
        user.getAccount().setAuthorities(listOfAuthorities);
        user.getAccount().setEmail(user.getAccount().getEmail().toLowerCase());
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        userService.addUser(user);
        return user;
    }

    public void sendConfirmationEmailMessage(User user) throws MailException {
        mailService.sendConfirmationMail(user);
    }
}


