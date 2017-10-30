package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.LearningRegime;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    public LearningRegime getLearningRegime() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getLearningRegime();
    }

    public void updateLearningRegime(LearningRegime learningRegime) throws NotAuthorisedUserException {
        Account account = accountRepository.findOne(userService.getAuthorizedUser().getAccount().getId());
        account.setLearningRegime(learningRegime);
        accountRepository.save(account);
    }

    public int getCardsNumber() throws NotAuthorisedUserException {
        return userService.getAuthorizedUser().getAccount().getCardsNumber();
    }

    public void updateCardsNumber(Integer cardsNumber) throws NotAuthorisedUserException {
        Account account = accountRepository.findOne(userService.getAuthorizedUser().getAccount().getId());
        if (cardsNumber < 1) {
            throw new IllegalArgumentException("Number of cards should be greater than 0.");
        }
        account.setCardsNumber(cardsNumber);
        accountRepository.save(account);
    }
}
