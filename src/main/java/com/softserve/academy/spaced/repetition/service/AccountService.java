package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

import java.util.List;

public interface AccountService {
    void updateAccount(Account account);

    LearningRegime getLearningRegime() throws NotAuthorisedUserException;

    void updateLearningRegime(String learningRegime) throws NotAuthorisedUserException, IllegalArgumentException;

    int getCardsNumber() throws NotAuthorisedUserException;

    void updateCardsNumber(Integer cardsNumber) throws NotAuthorisedUserException;

    List<RememberingLevel> getRememberingLevels() throws NotAuthorisedUserException;

    void updateRememberingLevel(Long levelId, int numberOfPostponedDays) throws NotAuthorisedUserException;

    void initializeLearningRegimeSettingsForAccount(Account account);

    void createNewAccountPassword(String email, String newPassword);

    String checkAccountStatusAndSendMail(String accountEmail);
}
