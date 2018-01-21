package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;

import java.util.Map;

/**
 * This interface proceeds all operations with email.
 */
public interface MailService {
    /**
     * Sends Email with confirmation of registration.
     *
     * @param user user to which email will be sent.
     */
    void sendConfirmationMail(User user);

    /**
     * Sends Email with activation message.
     *
     * @param email email on which message will be sent.
     */
    void sendActivationMail(String email);

    /**
     * Sends Email with password`s changing message.
     *
     * @param user user to which email will be sent.
     */

    void sendPasswordNotificationMail(User user);

    /**
     * Sends Email with password`s restoring message.
     *
     * @param accountEmail email on which message will be sent.
     */
    void sendPasswordRestoreMail(String accountEmail);

    /**
     * Generates content of the mail message.
     *
     * @param mailTemplate string by which will be got html template for mail.
     * @param model map with values which will be inserted into html page.
     * @return content of the email.
     */
    String getTemplateContentForMail(String mailTemplate, Map<String, Object> model);

}
