package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.service.validators.NumberOfPostponedDaysValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    public static final int NUMBER_OF_REMEMBERING_LEVELS = 6;
    private final AccountRepository accountRepository;
    private final RememberingLevelRepository rememberingLevelRepository;
    private final UserService userService;
    private final NumberOfPostponedDaysValidator numberOfPostponedDaysValidator;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, RememberingLevelRepository rememberingLevelRepository,
                          UserService userService, NumberOfPostponedDaysValidator numberOfPostponedDaysValidator,
                          MailService mailService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.rememberingLevelRepository = rememberingLevelRepository;
        this.userService = userService;
        this.numberOfPostponedDaysValidator = numberOfPostponedDaysValidator;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional
    public LearningRegime getLearningRegime() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getLearningRegime();
    }

    @Transactional
    public void updateLearningRegime(LearningRegime learningRegime) throws NotAuthorisedUserException {
        Account account = accountRepository.findOne(userService.getAuthorizedUser().getAccount().getId());
        account.setLearningRegime(learningRegime);
        accountRepository.save(account);
    }

    @Transactional
    public int getCardsNumber() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getCardsNumber();
    }

    @Transactional
    public void updateCardsNumber(Integer cardsNumber) throws NotAuthorisedUserException {
        Account account = userService.getAuthorizedUser().getAccount();
        if (cardsNumber < 1) {
            throw new IllegalArgumentException("Number of cards should be greater than 0.");
        }
        account.setCardsNumber(cardsNumber);
        accountRepository.save(account);
    }

    @Transactional
    public List<RememberingLevel> getRememberingLevels() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getRememberingLevels();
    }

    @Transactional
    public void updateRememberingLevel(Long levelId, int numberOfPostponedDays) throws NotAuthorisedUserException {
        RememberingLevel rememberingLevel = rememberingLevelRepository.findOne(levelId);
        numberOfPostponedDaysValidator.validate(rememberingLevel, numberOfPostponedDays);
        rememberingLevel.setNumberOfPostponedDays(numberOfPostponedDays);
        rememberingLevelRepository.save(rememberingLevel);
    }

    @Transactional
    public void initializeLearningRegimeSettingsForAccount(Account account) {
        rememberingLevelRepository.save(new RememberingLevel(1, "Teapot", 1, account));
        rememberingLevelRepository.save(new RememberingLevel(2, "Monkey", 3, account));
        rememberingLevelRepository.save(new RememberingLevel(3, "Beginner", 7, account));
        rememberingLevelRepository.save(new RememberingLevel(4, "Student", 14, account));
        rememberingLevelRepository.save(new RememberingLevel(5, "Expert", 30, account));
        rememberingLevelRepository.save(new RememberingLevel(6, "Genius", 60, account));
    }


    public void createNewAccountPassword(String email, String newPassword) {
        LOGGER.debug("Create new password for: {}", email);
        Account account = accountRepository.findByEmail(email);
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

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
