package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.service.JwtTokenForMailService;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.NoSuchElementException;

@Service
public class AccountVerificationByEmailServiceImpl implements AccountVerificationByEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountVerificationByEmailServiceImpl.class);
    @Autowired
    private JwtTokenForMailService jwtTokenForMailService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    public void accountVerification(String token) {
        String email;
        email = jwtTokenForMailService.decryptToken(token);
        if (userRepository.findUserByAccountEmail(email) == null) {
            throw new NoSuchElementException(messageSource.getMessage("message.exception.emailNotExist",
                    new Object[]{}, locale));
        }
        Account editedAcc = userRepository.findUserByAccountEmail(email).getAccount();
        editedAcc.setDeactivated(false);
        accountRepository.save(editedAcc);
    }

    @Override
    public String getAccountEmail(String token) {
        LOGGER.debug("Get account email from token ");
        return jwtTokenForMailService.getAccountEmailFromToken(token);
    }
}
