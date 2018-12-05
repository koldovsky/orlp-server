package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.mail.MailException;

/**
 * This interface supports the registration of the new user
 */
public interface RegistrationService {
    /**
     * Registers new user
     *
     * @param user user which is registering.
     * @return managed by admin user.
     */

    User registerNewUser(User user);

    /**
     * Sends Email with confirmation of registration.
     *
     * @param user user to which email will be sent.
     * @throws MailException if Email is incorrect.
     */

    void sendConfirmationEmailMessage(User user) throws MailException;

}
