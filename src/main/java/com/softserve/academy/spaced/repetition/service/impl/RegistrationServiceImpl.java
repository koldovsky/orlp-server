package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final int DEFAULT_POINTS_VALUE = 0;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
    public User registerNewUser(User user) {
        Account account = user.getAccount();
        userService.initializeNewUser(account, account.getEmail().toLowerCase(), AccountStatus.ACTIVE,
                true, AuthenticationType.LOCAL);
        user.getPerson().setImageType(ImageType.NONE);
        user.setFolder(new Folder());
        user.setPoints(DEFAULT_POINTS_VALUE);
        userService.addUser(user);
        accountService.initializeLearningRegimeSettingsForAccount(account);
        return user;
    }

    @Override
    public void sendConfirmationEmailMessage(User user) throws MailException {
        mailService.sendConfirmationMail(user);
    }

}
