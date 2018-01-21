package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
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

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
@RunWith(MockitoJUnitRunner.class)
@Transactional
public class AccountServiceImplTest {


    private final Long ACCOUNT_ID = 1L;
    private final Long USER_ID = 1L;
    private final String PASSWORD = "pass";
    private final String EMAIL = "account@test.com";
    private final Integer CARDS_NUMBER = 10;

    private Account account;
    private User user;
    private RememberingLevel rememberingLevel;

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


    @Before
    public void setUp() {
        Person person = DomainFactory.createPerson(1L,"","",null,"","");
        Set<Authority> authorities = new HashSet<>();
        List<Account> accounts = new ArrayList<>();
        rememberingLevel = new RememberingLevel();
        rememberingLevel.setNumberOfPostponedDays(1);
        rememberingLevel.setAccount(account);
        rememberingLevel.setId(1L);
        rememberingLevel.setName("Teapot");
        rememberingLevel.setOrderNumber(1);
        accounts.add(account);
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName(AuthorityName.ROLE_USER);
        authority.setAccounts(accounts);
        account = DomainFactory.createAccount(ACCOUNT_ID,PASSWORD,EMAIL, AuthenticationType.LOCAL, AccountStatus.ACTIVE
                ,true,new Date(),authorities, LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING,CARDS_NUMBER
                ,null);
        user = DomainFactory.createUser(USER_ID, account, person, null, null);
    }

    @Test
    public void testUpdateAccount() {
        when(accountRepository.save(account)).thenReturn(account);

        accountService.updateAccount(account);
        verify(accountRepository).save(account);
    }

    @Test
    public void getLearningRegime() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser().getAccount().getLearningRegime())
                .thenReturn(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetLearningRegimeByNotAuthorisedUser() throws NotAuthorisedUserException {

    }

    @Test
    public void updateLearningRegime() throws NotAuthorisedUserException{

    }

    @Test(expected = NotAuthorisedUserException.class)
    public void updateLearningRegimeNotAuthorisedUser() throws NotAuthorisedUserException {

    }

    @Test
    public void getCardsNumber() throws NotAuthorisedUserException {
    }

    @Test
    public void updateCardsNumber() throws NotAuthorisedUserException {
    }

    @Test
    public void getRememberingLevels() throws NotAuthorisedUserException {
    }

    @Test
    public void updateRememberingLevel() throws NotAuthorisedUserException {
    }

    @Test
    public void initializeLearningRegimeSettingsForAccount() {
    }

    @Test
    public void createNewAccountPassword() {
        when(accountRepository.findByEmail(EMAIL)).thenReturn(account);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(accountRepository.save(account)).thenReturn(account);

        accountService.createNewAccountPassword(EMAIL,PASSWORD);
        verify(accountRepository).findByEmail(EMAIL);
        verify(passwordEncoder).encode(PASSWORD);
        verify(accountRepository).save(account);
    }

    @Test
    public void testCheckAccountStatusAndSendMail() {

    }


















}