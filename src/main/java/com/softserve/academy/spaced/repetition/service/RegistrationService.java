package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.service.validators.AbstractValidator;
import com.softserve.academy.spaced.repetition.service.validators.BlankFieldValidator;
import com.softserve.academy.spaced.repetition.service.validators.EmailUniquesValidator;
import com.softserve.academy.spaced.repetition.service.validators.NullFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    @Autowired
    private AuthorityRepository authorityRepository;
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

    public User registerNewUser(User user) {
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
        user.getAccount().setLastPasswordResetDate(Calendar.getInstance().getTime());
        user.setFolder(new Folder());
        user.getAccount().setStatus(AccountStatus.INACTIVE);
        user.getAccount().setAuthenticationType(AuthenticationType.LOCAL);
        Authority authority = authorityRepository.findAuthorityByName(AuthorityName.ROLE_USER);
        user.getAccount().setAuthorities(Collections.singleton(authority));
        user.getAccount().setLearningRegime(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        user.getAccount().setEmail(user.getAccount().getEmail().toLowerCase());
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        user.getPerson().setTypeImage(ImageType.NONE);
        userService.addUser(user);
        return user;
    }

    public void sendConfirmationEmailMessage(User user) throws MailException {
        mailService.sendConfirmationMail(user);
    }
}


