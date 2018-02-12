package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

/**
 * Works with accounts of users
 */
public interface AccountService {
    /**
     * Updates account details
     *
     * @param account updated account
     */
    Account updateAccountDetails(Account account) throws NotAuthorisedUserException;

    /**
     * Returns quantity of cards for studying for current user.
     *
     * @return updated account
     * @throws NotAuthorisedUserException if user is not authorised
     */
    Account getAccountDetails() throws NotAuthorisedUserException;

    /**
     * Returns quantity of cards for studying for current user.
     *
     * @return quantity of cards (10 by default)
     * @throws NotAuthorisedUserException if user is not authorised
     */
    int getCardsNumber() throws NotAuthorisedUserException;

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
