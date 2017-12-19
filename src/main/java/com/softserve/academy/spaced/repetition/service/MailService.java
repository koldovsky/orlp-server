package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;

public interface MailService {
    void sendConfirmationMail(User user);

    void sendActivationMail(String email);

    void sendPasswordNotificationMail(User user);

    void sendPasswordRestoreMail(String accountEmail);
}
