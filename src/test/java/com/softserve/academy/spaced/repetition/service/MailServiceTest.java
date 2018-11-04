package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.MailDTO;
import com.softserve.academy.spaced.repetition.service.impl.MailServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;

import static org.hamcrest.core.IsInstanceOf.any;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailService mailService;

    @Mock
    private MailServiceImpl mailServiceImpl;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSendRequestFromContactUsFormToEmail(){
        MailDTO mailDTO = createNewMailDTO();
        MimeMessagePreparator preparator = mailServiceImpl.convertMailDTOToAFormattedLetter(mailDTO);
        doNothing().when(mailSender).send(preparator);
        mailSender.send(preparator);
        verify(mailSender, times(1)).send(refEq(preparator));
    }

    private MailDTO createNewMailDTO() {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setEmail("test@test.com");
        mailDTO.setName("Tester");
        mailDTO.setSubject("TestSubject");
        mailDTO.setText("TestText");
        return mailDTO;
    }

}
