package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.service.impl.MailServiceImpl;
import com.softserve.academy.spaced.repetition.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface RegistrationService {
    User registerNewUser(User user);

    void sendConfirmationEmailMessage(User user) throws MailException;

    @Autowired
    void setAuthorityRepository(AuthorityRepository authorityRepository);

    @Autowired
    void setUserService(UserServiceImpl userService);

    @Autowired
    void setPasswordEncoder(PasswordEncoder passwordEncoder);

    @Autowired
    void setMailService(MailServiceImpl mailService);

    @Autowired
    void setAccountService(AccountService accountService);
}
