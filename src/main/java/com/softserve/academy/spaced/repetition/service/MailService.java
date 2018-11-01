package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.MailDTO;
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
     * Sends data from contact us form on the website to a custom e-mail which you can set in configuration file
     * @param mailDTO DTO which contains name of the user, his e-mail, subject of user's request and his message
     */
    void sendRequestFromContactUsFormToEmail(MailDTO mailDTO);

    /**
     * Generates the text of email to verify the registration.
     *
     * @param model map with values which will be inserted into html page.
     * @return content of the email.
     */
    String getFreeMarkerTemplateContent(Map<String, Object> model);

    /**
     * Generates the text of email with account activation content.
     *
     * @param model map with values which will be inserted into html page.
     * @return content of the email.
     */
    String getActivationAccountTemplateContent(Map<String, Object> model);

    /**
     * Generates the text of email with password changing content.
     *
     * @param model map with values which will be inserted into html page.
     * @return content of the email.
     */
    String getChangePasswordTemplateContent(Map<String, Object> model);

    /**
     * Generates the text of email with password restoring content.
     *
     * @param model map with values which will be inserted into html page.
     * @return content of the email.
     */
    String getRestorePasswordTemplateContent(Map<String, Object> model);
}
