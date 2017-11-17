package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.security.JwtTokenForMail;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {
    @Value("${app.origin.url}")
    private String URL;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtTokenForMail jwtTokenForMail;
    @Autowired
    @Qualifier("freemarkerConf")
    private Configuration freemarkerConfiguration;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    public void sendConfirmationMail(User user) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("Confirmation registration");
            helper.setTo(user.getAccount().getEmail());
            Map <String, Object> model = new HashMap <>();
            String token = jwtTokenForMail.generateTokenForMail(user);
            model.put("person", user.getPerson());
            model.put("token", token);
            model.put("url", URL);
            String text = getFreeMarkerTemplateContent(model);
            helper.setText(text, true);
        };
        mailSender.send(preparator);
    }

    public void sendActivationMail(String email) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("Activation account");
            helper.setTo(email);
            Map <String, Object> model = new HashMap <>();
            String token = jwtTokenForMail.generateTokenForMailFromEmail(email);
            model.put("token", token);
            model.put("url", URL);
            String text = getActivationAccountTemplateContent(model);
            helper.setText(text, true);
        };
        mailSender.send(preparator);
    }

    public void sendPasswordNotificationMail(User user) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("Change password notification");
            helper.setTo(user.getAccount().getEmail());
            Map <String, Object> model = new HashMap <>();
            model.put("person", user.getPerson());
            model.put("datachange", Calendar.getInstance().getTime().toString());
            model.put("url", URL);
            String text = getChangePasswordTemplateContent(model);
            helper.setText(text, true);
        };
        mailSender.send(preparator);
    }

    private String getFreeMarkerTemplateContent(Map <String, Object> model) {
        StringBuilder content = new StringBuilder();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate("registrationVerificationMailTemplate.txt"), model));
            return content.toString();
        } catch (IOException | TemplateException e) {
            LOGGER.error("Couldn't generate email content.", e);
        }
        return "";
    }

    private String getActivationAccountTemplateContent(Map <String, Object> model) {
        StringBuilder content = new StringBuilder();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate("activationAccountMailTemplate.txt"), model));
            return content.toString();
        } catch (IOException | TemplateException e) {
            LOGGER.error("Couldn't generate email content.", e);
        }
        return "";
    }

    private String getChangePasswordTemplateContent(Map <String, Object> model) {
        StringBuilder content = new StringBuilder();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate("changePasswordMailTemplate.txt"), model));
            return content.toString();
        } catch (IOException | TemplateException e) {
            LOGGER.error("Couldn't generate email content.", e);
        }
        return "";
    }

}
