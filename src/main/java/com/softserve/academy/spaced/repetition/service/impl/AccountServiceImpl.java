package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    public static final int NUMBER_OF_REMEMBERING_LEVELS = 6;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RememberingLevelRepository rememberingLevelRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Account updateAccountDetails(Account acc) throws NotAuthorisedUserException {
        Account account = userService.getAuthorizedUser().getAccount();
        account.setRememberingLevels(acc.getRememberingLevels());
        account.setCardsNumber(acc.getCardsNumber());
        account.setLearningRegime(acc.getLearningRegime());
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account getAccountDetails() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount();
    }

    @Override
    @Transactional
    public int getCardsNumber() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getCardsNumber();
    }

    @Override
    @Transactional
    public void initializeLearningRegimeSettingsForAccount(Account account) {
        rememberingLevelRepository.save(new RememberingLevel(1, "Teapot", 1, account));
        rememberingLevelRepository.save(new RememberingLevel(2, "Monkey", 3, account));
        rememberingLevelRepository.save(new RememberingLevel(3, "Beginner", 7, account));
        rememberingLevelRepository.save(new RememberingLevel(4, "Student", 14, account));
        rememberingLevelRepository.save(new RememberingLevel(5, "Expert", 30, account));
        rememberingLevelRepository.save(new RememberingLevel(6, "Genius", 60, account));
    }

    @Override
    @Transactional
    public void createNewAccountPassword(String email, String newPassword) {
        LOGGER.debug("Create new password for: {}", email);
        Account account = accountRepository.findByEmail(email);
        account.setPassword(passwordEncoder.encode(newPassword));
        account.setLastPasswordResetDate(new Date());
        accountRepository.save(account);
    }

    @Override
    public String checkAccountStatusAndSendMail(String accountEmail) {
        LOGGER.debug("Check account status for: {}", accountEmail);
        Account account = accountRepository.findByEmail(accountEmail);
        if (account == null) {
            return "NOT_FOUND";
        }
        if (account.isDeactivated()) {
            return "DEACTIVATED";
        }
        if (account.getStatus() == AccountStatus.DELETED) {
            return "DELETED";
        }
        AuthenticationType type = account.getAuthenticationType();
        if (type == AuthenticationType.LOCAL) {
            mailService.sendPasswordRestoreMail(accountEmail);
            return "LOCAL";
        }
        return type.toString();
    }
}
