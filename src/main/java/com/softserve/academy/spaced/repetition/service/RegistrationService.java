package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface RegistrationService {
    User registerNewUser(User user);

    void sendConfirmationEmailMessage(User user) throws MailException;
}
