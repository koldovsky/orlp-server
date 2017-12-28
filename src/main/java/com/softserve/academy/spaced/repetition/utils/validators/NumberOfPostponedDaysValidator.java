package com.softserve.academy.spaced.repetition.utils.validators;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.stereotype.Service;

import static com.softserve.academy.spaced.repetition.service.impl.AccountServiceImpl.NUMBER_OF_REMEMBERING_LEVELS;

@Service
public class NumberOfPostponedDaysValidator {
    private final UserService userService;
    private final RememberingLevelRepository rememberingLevelRepository;

    public NumberOfPostponedDaysValidator(UserService userService, RememberingLevelRepository rememberingLevelRepository) {
        this.userService = userService;
        this.rememberingLevelRepository = rememberingLevelRepository;
    }

    public void validate(RememberingLevel level, int numberOfPostponedDays) throws NotAuthorisedUserException {
        Account account = userService.getAuthorizedUser().getAccount();
        if (numberOfPostponedDays < 1) {
            throw new IllegalArgumentException("Number of postponed days should be greater that 0.");
        }
        if (level.getOrderNumber() > 1) {
            RememberingLevel previousLevel = rememberingLevelRepository
                    .findRememberingLevelByAccountEqualsAndOrderNumber(account, level.getOrderNumber() - 1);
            if (numberOfPostponedDays <= previousLevel.getNumberOfPostponedDays()) {
                throw new IllegalArgumentException("Number of postponed days for this level should be greater than " +
                        "number of postponed days for a previous one.");
            }
        }
        if (level.getOrderNumber() < NUMBER_OF_REMEMBERING_LEVELS) {
            RememberingLevel nextLevel = rememberingLevelRepository
                    .findRememberingLevelByAccountEqualsAndOrderNumber(account, level.getOrderNumber() + 1);
            if (numberOfPostponedDays >= nextLevel.getNumberOfPostponedDays()) {
                throw new IllegalArgumentException("Number of postponed days for this level should be less than " +
                        "number of postponed days for a next one.");
            }
        }
    }
}
