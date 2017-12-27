package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface RegistrationService {
    User registerNewUser(User user);

    void sendConfirmationEmailMessage(User user) throws MailException;

    void setAuthorityRepository(AuthorityRepository authorityRepository);

    void setUserService(UserService userService);

    void setPasswordEncoder(PasswordEncoder passwordEncoder);

    void setMailService(MailService mailService);

    void setAccountService(AccountService accountService);
}
