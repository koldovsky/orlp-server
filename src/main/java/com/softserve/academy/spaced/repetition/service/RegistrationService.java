package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class RegistrationService {

    private AuthorityRepository authorityRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;

    private AccountServiceImpl accountServiceImpl;

    public User registerNewUser(User user) {
        Account account = user.getAccount();
        userService.initializeNewUser(account, account.getEmail().toLowerCase(), AccountStatus.ACTIVE,
               true, AuthenticationType.LOCAL);
        user.getPerson().setTypeImage(ImageType.NONE);
        user.setFolder(new Folder());
        userService.addUser(user);
        accountServiceImpl.initializeLearningRegimeSettingsForAccount(account);
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
    public void setAccountServiceImpl(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }
}
