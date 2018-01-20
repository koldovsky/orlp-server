package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtTokenForMail;
import com.softserve.academy.spaced.repetition.service.impl.AccountVerificationByEmailServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class AccountVerificationByEmailServiceImplTest {
    private final String EMAIL = "account@test.com";
    @Mock
    private JwtTokenForMail jwtTokenForMail;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AccountVerificationByEmailServiceImpl accountVerificationByEmailService;
    private User user;
    private Account account;

    @Before
    public void setUp() {
        account = DomainFactory.createAccount(1L, "", EMAIL, null, null,
                true, null, null, null, null, null);
        user = DomainFactory.createUser(1L, account, null, null, null);
    }

    @Test
    public void testAccountVerification() {
        when(jwtTokenForMail.decryptToken("")).thenReturn(EMAIL);
        when(userRepository.findUserByAccountEmail(EMAIL)).thenReturn(user);

        accountVerificationByEmailService.accountVerification("");
        verify(jwtTokenForMail).decryptToken("");
        verify(userRepository,times(2)).findUserByAccountEmail(EMAIL);
        validateMockitoUsage();

    }

    @Test
    public void testGetAccountEmail() {
        when(jwtTokenForMail.getAccountEmailFromToken("")).thenReturn("");

        accountVerificationByEmailService.getAccountEmail("");
        verify(jwtTokenForMail).getAccountEmailFromToken("");

    }
}