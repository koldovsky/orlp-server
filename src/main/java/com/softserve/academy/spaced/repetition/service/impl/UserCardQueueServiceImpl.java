package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.enums.UserCardQueueStatus;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

import static com.softserve.academy.spaced.repetition.service.impl.AccountServiceImpl.NUMBER_OF_REMEMBERING_LEVELS;

@Service
public class UserCardQueueServiceImpl implements UserCardQueueService {
    private static final int DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

    @Autowired
    private UserCardQueueRepository userCardQueueRepository;

    @Autowired
    private RememberingLevelRepository rememberingLevelRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void updateUserCardQueue(Long deckId, Long cardId, String status)
            throws NotAuthorisedUserException, IllegalArgumentException {
        boolean userCardQueueStatusFound = Arrays.stream(UserCardQueueStatus.values())
                .anyMatch(UserCardQueueStatus.valueOf(status)::equals);
        if(!userCardQueueStatusFound) {
            throw new IllegalArgumentException("Value of User Card Queue Status is not valid: " + status);
        }

        User user = userService.getAuthorizedUser();
        UserCardQueueStatus userCardQueueStatus = UserCardQueueStatus.valueOf(status);
        Long userId = user.getId();
        UserCardQueue userCardQueue = userCardQueueRepository.findUserCardQueueByUserIdAndCardId(userId, cardId);
        if (userCardQueue == null) {
            userCardQueue = new UserCardQueue();
            userCardQueue.setCardId(cardId);
            userCardQueue.setDeckId(deckId);
            userCardQueue.setUserId(userId);
        }
        userCardQueue.setCardDate(new Date());

        Account account = user.getAccount();
        LearningRegime learningRegime = account.getLearningRegime();
        if (learningRegime == LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING) {
            userCardQueue.setStatus(userCardQueueStatus);
        } else if (learningRegime == LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION) {
            applyCardsPostponingLearningRegime(userCardQueue, userCardQueueStatus, account);
        }
        userCardQueueRepository.save(userCardQueue);
    }

    private void applyCardsPostponingLearningRegime(UserCardQueue userCardQueue, UserCardQueueStatus status,
                                                    Account account) {
        RememberingLevel userCardQueueRememberingLevel = userCardQueue.getRememberingLevel();
        RememberingLevel rememberingLevel =
                rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account, 1);
        if (userCardQueueRememberingLevel != null) {
            rememberingLevel = userCardQueueRememberingLevel;
        }

        Integer orderedNumber = rememberingLevel.getOrderNumber();
        if (status == UserCardQueueStatus.BAD && orderedNumber > 1) {
            rememberingLevel = rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account,
                    --orderedNumber);
        } else if (status == UserCardQueueStatus.GOOD && orderedNumber < NUMBER_OF_REMEMBERING_LEVELS) {
            rememberingLevel = rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account,
                    ++orderedNumber);
        }
        userCardQueue.setRememberingLevel(rememberingLevel);

        Date cardDate = userCardQueue.getCardDate();
        Long cardDateTime = cardDate.getTime();
        Integer numberOfPostponedDays = userCardQueueRememberingLevel.getNumberOfPostponedDays();
        Date dateToRepeat = new Date(cardDateTime + numberOfPostponedDays * DAY_IN_MILLISECONDS);
        userCardQueue.setDateToRepeat(dateToRepeat);
    }

    @Override
    public UserCardQueue getUserCardQueueById(long id) {
        return userCardQueueRepository.findOne(id);
    }

    @Override
    @Transactional
    public long countCardsThatNeedRepeating(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Long userId = user.getId();
        return userCardQueueRepository.countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(userId, deckId, new Date());
    }
}
