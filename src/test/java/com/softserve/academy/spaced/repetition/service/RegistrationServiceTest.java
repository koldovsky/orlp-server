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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class RegistrationServiceTest {

    private final String ACCOUNT_EMAIL = "account@test.com";
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;
    @Mock
    private AccountService accountService;
    private User user;
    private Account account;

    @Before
    public void setUp() {
        final Long USER_ID = 1L;
        final Long PERSON_ID = 1L;
        final Long ACCOUNT_ID = 1L;

        final Person person = DomainFactory.createPerson(PERSON_ID, null, null, null, null, null);
        account = DomainFactory.createAccount(ACCOUNT_ID, null, ACCOUNT_EMAIL, null, null, false, null, null, null,
                null, null);
        user = DomainFactory.createUser(USER_ID, account, person, null, null);

        doNothing().when(userService).addUser(user);
        doNothing().when(accountService).initializeLearningRegimeSettingsForAccount(account);
        doNothing().when(mailService).sendConfirmationMail(user);
    }

    @Test
    public void testRegisterNewUser() {
        final AccountStatus ACCOUNT_STATUS_ACTIVE = AccountStatus.ACTIVE;
        final boolean ACCOUNT_DEACTIVATED = true;
        final AuthenticationType AUTHENTICATION_TYPE = AuthenticationType.LOCAL;

        doNothing().when(userService).initializeNewUser(account, ACCOUNT_EMAIL, ACCOUNT_STATUS_ACTIVE,
                ACCOUNT_DEACTIVATED, AUTHENTICATION_TYPE);

        User result = registrationService.registerNewUser(user);
        verify(userService).initializeNewUser(account, ACCOUNT_EMAIL, ACCOUNT_STATUS_ACTIVE, ACCOUNT_DEACTIVATED,
                AUTHENTICATION_TYPE);
        verify(userService).addUser(user);
        verify(accountService).initializeLearningRegimeSettingsForAccount(account);
        assertEquals(user, result);
    }

    @Test
    public void testSendConfirmationEmailMessage() {
        registrationService.sendConfirmationEmailMessage(user);
        verify(mailService).sendConfirmationMail(user);
    }
}
