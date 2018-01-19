package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

import java.util.List;

/**
 * Works with accounts of users
 */
public interface AccountService {
    /**
     * Updates account
     *
     * @param account updated account
     */
    void updateAccount(Account account);

    /**
     * Returns learning regime of current user.
     *
     * @return learning regime
     * @throws NotAuthorisedUserException if user is not authorised
     */
    LearningRegime getLearningRegime() throws NotAuthorisedUserException;

    /**
     * Changes learning regime for current user.
     *
     * @param learningRegime must not be {@literal null}.
     * @throws NotAuthorisedUserException if user is not authorised
     * @throws IllegalArgumentException   if value of {@code learningRegime} is not valid
     */
    void updateLearningRegime(String learningRegime) throws NotAuthorisedUserException, IllegalArgumentException;

    /**
     * Returns quantity of cards for studying for current user.
     *
     * @return quantity of cards (10 by default)
     * @throws NotAuthorisedUserException if user is not authorised
     */
    int getCardsNumber() throws NotAuthorisedUserException;

    /**
     * Updates quantity of cards for studying for current user.
     *
     * @param cardsNumber new quantity of cards for studying
     * @throws NotAuthorisedUserException if user is not authorised
     */
    void updateCardsNumber(Integer cardsNumber) throws NotAuthorisedUserException;

    /**
     * Returns remembering levels for current user.
     *
     * @return list of remembering levels
     * @throws NotAuthorisedUserException if user is not authorised
     */
    List<RememberingLevel> getRememberingLevels() throws NotAuthorisedUserException;

    /**
     * Updates remembering level with given identifier for current user.
     *
     * @param levelId               must not be {@literal null}.
     * @param numberOfPostponedDays new number of postponed days
     * @throws NotAuthorisedUserException if user is not authorised
     */
    void updateRememberingLevel(Long levelId, int numberOfPostponedDays) throws NotAuthorisedUserException;

    /**
     * Initialize learning regime settings for account in registration time.
     *
     * @param account that was registered
     */
    void initializeLearningRegimeSettingsForAccount(Account account);

    /**
     * Changes password for account with given email
     *
     * @param email       of account that needs changing password
     * @param newPassword must not be {null}.
     */
    void createNewAccountPassword(String email, String newPassword);

    /**
     * Check account status and send mail for restoring password
     *
     * @param accountEmail user's email
     * @return account status
     */
    String checkAccountStatusAndSendMail(String accountEmail);
}
