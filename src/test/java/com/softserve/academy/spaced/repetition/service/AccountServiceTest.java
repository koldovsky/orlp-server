package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.service.impl.AccountServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.validators.NumberOfPostponedDaysValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class AccountServiceTest {

    private final Long ACCOUNT_ID = 1L;
    private final String PASSWORD = "pass";
    private final String EMAIL = "account@test.com";
    private final Integer CARDS_NUMBER = 10;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RememberingLevelRepository rememberingLevelRepository;
    @Mock
    private UserService userService;
    @Mock
    private NumberOfPostponedDaysValidator numberOfPostponedDaysValidator;
    @Mock
    private MailService mailService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountServiceImpl accountService;
    private Account account;


    @Before
    public void setUp() throws Exception {
        Person person = DomainFactory.createPerson(1L, "", "", null, "", "");
        account = DomainFactory.createAccount(ACCOUNT_ID, PASSWORD, EMAIL, AuthenticationType.LOCAL, AccountStatus.ACTIVE
                , false, new Date(), null, LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING, CARDS_NUMBER
                , null);
        User user = DomainFactory.createUser(1L, account, person, null, null);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(accountRepository.save(account)).thenReturn(account);
    }

    @Test
    public void testUpdateAccount() {
        accountService.updateAccount(account);
        verify(accountRepository).save(account);
    }

    @Test
    public void testGetLearningRegime() throws NotAuthorisedUserException {

        LearningRegime result = accountService.getLearningRegime();
        verify(userService).getAuthorizedUser();
        assertEquals(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetLearningRegimeByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        accountService.getLearningRegime();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testUpdateLearningRegime() throws NotAuthorisedUserException, IllegalArgumentException {
        when(accountRepository.findOne(ACCOUNT_ID)).thenReturn(account);

        accountService.updateLearningRegime("BAD_NORMAL_GOOD_STATUS_DEPENDING");
        verify(accountRepository).findOne(ACCOUNT_ID);
        verify(userService).getAuthorizedUser();
        verify(accountRepository).save(account);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void updateLearningRegimeByNotAuthorisedUser() throws NotAuthorisedUserException, IllegalArgumentException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        accountService.updateLearningRegime("BAD_NORMAL_GOOD_STATUS_DEPENDING");
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetCardsNumber() throws NotAuthorisedUserException {
        accountService.getCardsNumber();
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetCardsNumberByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        accountService.getCardsNumber();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testUpdateCardsNumber() throws NotAuthorisedUserException {
        accountService.updateCardsNumber(CARDS_NUMBER);
        verify(userService).getAuthorizedUser();
        verify(accountRepository).save(account);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void updateCardsNumber() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        accountService.updateCardsNumber(CARDS_NUMBER);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetRememberingLevels() throws NotAuthorisedUserException {
        List<RememberingLevel> result = accountService.getRememberingLevels();
        verify(userService).getAuthorizedUser();
        assertEquals(null, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetRememberingLevelsByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        accountService.updateCardsNumber(CARDS_NUMBER);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testUpdateRememberingLevel() throws NotAuthorisedUserException {
        RememberingLevel rememberingLevel = new RememberingLevel();
        Long rememberingLevelId = 1L;
        rememberingLevel.setId(rememberingLevelId);
        rememberingLevel.setNumberOfPostponedDays(5);

        when(rememberingLevelRepository.findOne(rememberingLevelId)).thenReturn(rememberingLevel);
        doNothing().when(numberOfPostponedDaysValidator).validate(rememberingLevel, 5);
        when(rememberingLevelRepository.save(rememberingLevel)).thenReturn(null);

        accountService.updateRememberingLevel(rememberingLevelId, 5);
        verify(rememberingLevelRepository).findOne(rememberingLevelId);
        verify(numberOfPostponedDaysValidator).validate(rememberingLevel, 5);
        verify(rememberingLevelRepository).save(rememberingLevel);
    }

    @Test
    public void testInitializeLearningRegimeSettingsForAccount() {
        when(rememberingLevelRepository.save(any(RememberingLevel.class))).thenReturn(any(RememberingLevel.class));

        accountService.initializeLearningRegimeSettingsForAccount(account);
        verify(rememberingLevelRepository, times(6)).save(any(RememberingLevel.class));
    }

    @Test
    public void createNewAccountPassword() {
        when(accountRepository.findByEmail(EMAIL)).thenReturn(account);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(accountRepository.save(account)).thenReturn(account);

        accountService.createNewAccountPassword(EMAIL, PASSWORD);
        verify(accountRepository).findByEmail(EMAIL);
        verify(passwordEncoder).encode(PASSWORD);
        verify(accountRepository).save(account);
    }

    @Test
    public void testCheckAccountStatusAndSendMail() {
        when(accountRepository.findByEmail(EMAIL)).thenReturn(account);
        doNothing().when(mailService).sendPasswordRestoreMail(EMAIL);

        String result = accountService.checkAccountStatusAndSendMail(EMAIL);
        verify(accountRepository).findByEmail(EMAIL);
        verify(mailService).sendPasswordRestoreMail(EMAIL);
        assertEquals("LOCAL", result);
    }
}