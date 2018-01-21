package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.service.impl.RegistrationServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.autoconfigure.ShellProperties;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class RegistrationServiceTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private UserService userService;

    @Mock
    private MailService mailService;

    @Mock
    private AccountService accountService;

    private User user;
    private Person person;
    private Account account;

    private final long USER_ID = 1L;
    private final long PERSON_ID = 1L;
    private final long ACCOUNT_ID = 1L;
    private final String ACCOUNT_EMAIL = "account@email.test";

    @Before
    public void setUp() {
        person = DomainFactory.createPerson(PERSON_ID, null, null, null, null, null);
        account = DomainFactory.createAccount(ACCOUNT_ID, null, ACCOUNT_EMAIL, null, null, false, null ,null ,null,
                null, null);
        user = DomainFactory.createUser(USER_ID, account, person, null, null);
    }

    @Test
    public void testRegisterNewUser() {
        doNothing().when(userService).initializeNewUser(eq(account), eq(ACCOUNT_EMAIL), any(AccountStatus.class),
                any(boolean.class), any(AuthenticationType.class));
        doNothing().when(userService).addUser(user);
        doNothing().when(accountService).initializeLearningRegimeSettingsForAccount(account);

        User result = registrationService.registerNewUser(user);
        verify(userService).initializeNewUser(eq(account), eq(ACCOUNT_EMAIL), any(AccountStatus.class), any(boolean.class),
                any(AuthenticationType.class));
        verify(userService).addUser(user);
        verify(accountService).initializeLearningRegimeSettingsForAccount(account);
        assertEquals(user, result);
    }

    @Test
    public void testSendConfirmationEmailMessage() {
        doNothing().when(mailService).sendConfirmationMail(user);

        registrationService.sendConfirmationEmailMessage(user);
        verify(mailService).sendConfirmationMail(user);
    }
}
