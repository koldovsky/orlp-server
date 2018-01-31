package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.validators.NumberOfPostponedDaysValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private NumberOfPostponedDaysValidator numberOfPostponedDaysValidator;
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public LearningRegime getLearningRegime() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getLearningRegime();
    }

    @Override
    @Transactional
    public void updateLearningRegime(String learningRegime) throws NotAuthorisedUserException,
            IllegalArgumentException {
        boolean learningRegimeFound = Arrays.stream(LearningRegime.values())
                .anyMatch(LearningRegime.valueOf(learningRegime)::equals);

        if(!learningRegimeFound) {
            throw new IllegalArgumentException(messageSource.getMessage("message.exception.learningRegimeNotValid",
                    new Object[]{learningRegime}, locale));
        }

        LearningRegime regime = LearningRegime.valueOf(learningRegime);

        Account account = accountRepository.findOne(userService.getAuthorizedUser().getAccount().getId());
        account.setLearningRegime(regime);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public int getCardsNumber() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getCardsNumber();
    }

    @Override
    @Transactional
    public void updateCardsNumber(Integer cardsNumber) throws NotAuthorisedUserException {
        Account account = userService.getAuthorizedUser().getAccount();
        if (cardsNumber < 1) {
            throw new IllegalArgumentException(messageSource.getMessage("message.exception.numbersOfCardsNegative",
                    new Object[]{}, locale));
        }
        account.setCardsNumber(cardsNumber);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public List<RememberingLevel> getRememberingLevels() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getRememberingLevels();
    }

    @Override
    @Transactional
    public void updateRememberingLevel(Long levelId, int numberOfPostponedDays) throws NotAuthorisedUserException {
        RememberingLevel rememberingLevel = rememberingLevelRepository.findOne(levelId);
        numberOfPostponedDaysValidator.validate(rememberingLevel, numberOfPostponedDays);
        rememberingLevel.setNumberOfPostponedDays(numberOfPostponedDays);
        rememberingLevelRepository.save(rememberingLevel);
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
