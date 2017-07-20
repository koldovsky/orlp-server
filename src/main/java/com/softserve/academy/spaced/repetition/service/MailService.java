package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.security.JwtTokenForMail;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {
    private static final String URL = "http://localhost:3000";


    @Autowired
    JavaMailSender mailSender;

    @Autowired
    JwtTokenForMail jwtTokenForMail;

    @Autowired
    @Qualifier("freemarkerConf")
    Configuration freemarkerConfiguration;

    public void sendMail(User user) throws MailException {
        MimeMessagePreparator preparator = getMessagePreparator(user);
        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
        }
    }

    private MimeMessagePreparator getMessagePreparator(User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setSubject("Successful registration");
                helper.setTo(user.getAccount().getEmail());
                Map <String, Object> model = new HashMap <String, Object>();
                String token = jwtTokenForMail.generateTokenForMail(user);
                model.put("person", user.getPerson());
                model.put("token", token);
                model.put("url", URL);
                String text = getFreeMarkerTemplateContent(model);
                helper.setText(text, true);
            }
        };
        return preparator;
    }

    private String getFreeMarkerTemplateContent(Map <String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfiguration.getTemplate("registrationVerificationMailTemplate.txt"), model));
            return content.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (TemplateException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}