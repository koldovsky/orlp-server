package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;

import java.util.Map;


public interface MailService {

    void sendConfirmationMail(User user);

    void sendActivationMail(String email);

    void sendPasswordNotificationMail(User user);

    void sendPasswordRestoreMail(String accountEmail);

    String getFreeMarkerTemplateContent(Map<String, Object> model);

    String getActivationAccountTemplateContent(Map<String, Object> model);

    String getChangePasswordTemplateContent(Map<String, Object> model);

    String getRestorePasswordTemplateContent(Map<String, Object> model);

}
