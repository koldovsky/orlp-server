package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.security.JwtTokenForMail;
import com.softserve.academy.spaced.repetition.service.impl.MailServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import freemarker.template.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class MailServiceTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private JwtTokenForMail jwtTokenForMail;

    @Mock
    @Qualifier("freemarkerConf")
    private Configuration configuration;

    private User user;
    private Account account;
    private Person person;

    private final long USER_ID = 1L;
    private final long ACCOUNT_ID = 1L;
    private final String ACCOUNT_EMAIL = "account@test.mail";
    private final long PERSON_ID = 1L;
    private final String TOKEN = "token";

    @Before
    public void setUp() {
        person = DomainFactory.createPerson(PERSON_ID, null, null, null, null, null);
        account = DomainFactory.createAccount(ACCOUNT_ID, null, ACCOUNT_EMAIL, null, null, false, null, null ,null,
                null, null);
        user = DomainFactory.createUser(USER_ID, account, person, null, null);
    }

    @Test
    public void testSendConfirmationMail() {
        when(jwtTokenForMail.generateTokenForMail(ACCOUNT_EMAIL)).thenReturn(TOKEN);
        doNothing().when(javaMailSender).send(any(MimeMessagePreparator.class));

        mailService.sendConfirmationMail(user);
        verify(jwtTokenForMail).generateTokenForMail(ACCOUNT_EMAIL);
        verify(javaMailSender).send(any(MimeMessagePreparator.class));
    }
}
