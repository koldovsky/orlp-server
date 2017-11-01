package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
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

import static com.softserve.academy.spaced.repetition.domain.Account.CARDS_NUMBER;

@Service
public class RegistrationService {

    private AuthorityRepository authorityRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;
    private EmailUniquesValidator emailUniquesValidator;
    private BlankFieldValidator blankFieldValidator;
    private NullFieldsValidator nullFieldsValidator;
    private RememberingLevelRepository rememberingLevelRepository;

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
        user.getAccount().setCardsNumber(CARDS_NUMBER);
        user.getAccount().setEmail(user.getAccount().getEmail().toLowerCase());
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        user.getPerson().setTypeImage(ImageType.NONE);
        userService.addUser(user);
        rememberingLevelRepository.save(new RememberingLevel(1, "Teapot", 1, user.getAccount()));
        rememberingLevelRepository.save(new RememberingLevel(2, "Monkey", 3, user.getAccount()));
        rememberingLevelRepository.save(new RememberingLevel(3, "Beginner", 7, user.getAccount()));
        rememberingLevelRepository.save(new RememberingLevel(4, "Student", 14, user.getAccount()));
        rememberingLevelRepository.save(new RememberingLevel(5, "Expert", 30, user.getAccount()));
        rememberingLevelRepository.save(new RememberingLevel(6, "Genius", 60, user.getAccount()));
        return user;
    }

    public void sendConfirmationEmailMessage(User user) throws MailException {
        mailService.sendConfirmationMail(user);
    }

    @Autowired
    public void setAuthorityRepository(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setEmailUniquesValidator(EmailUniquesValidator emailUniquesValidator) {
        this.emailUniquesValidator = emailUniquesValidator;
    }

    @Autowired
    public void setBlankFieldValidator(BlankFieldValidator blankFieldValidator) {
        this.blankFieldValidator = blankFieldValidator;
    }

    @Autowired
    public void setNullFieldsValidator(NullFieldsValidator nullFieldsValidator) {
        this.nullFieldsValidator = nullFieldsValidator;
    }

    @Autowired
    public void setRememberingLevelRepository(RememberingLevelRepository rememberingLevelRepository) {
        this.rememberingLevelRepository = rememberingLevelRepository;
    }
}
