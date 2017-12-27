package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.softserve.academy.spaced.repetition.service.impl.AccountServiceImpl.NUMBER_OF_REMEMBERING_LEVELS;

@Service
public class UserCardQueueServiceImpl implements UserCardQueueService {
    private static final int DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
    private final UserCardQueueRepository userCardQueueRepository;
    private final UserService userService;
    private final RememberingLevelRepository rememberingLevelRepository;

    @Autowired
    public UserCardQueueServiceImpl(UserCardQueueRepository userCardQueueRepository, UserService userService, RememberingLevelRepository rememberingLevelRepository) {
        this.userCardQueueRepository = userCardQueueRepository;
        this.userService = userService;
        this.rememberingLevelRepository = rememberingLevelRepository;
    }

    @Override
    @Transactional
    public void updateUserCardQueue(Long deckId, Long cardId, UserCardQueueStatus userCardQueueStatus)
            throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        UserCardQueue userCardQueue = userCardQueueRepository.findUserCardQueueByUserIdAndCardId(user.getId(), cardId);
        if (userCardQueue == null) {
            userCardQueue = new UserCardQueue();
            userCardQueue.setCardId(cardId);
            userCardQueue.setDeckId(deckId);
            userCardQueue.setUserId(user.getId());
        }
        userCardQueue.setCardDate(new Date());

        if (user.getAccount().getLearningRegime() == LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING) {
            userCardQueue.setStatus(userCardQueueStatus);
        } else if (user.getAccount().getLearningRegime() == LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION) {
            applyCardsPostponingLearningRegime(userCardQueue, userCardQueueStatus);
        }
        userCardQueueRepository.save(userCardQueue);
    }

    private void applyCardsPostponingLearningRegime(UserCardQueue userCardQueue, UserCardQueueStatus status)
            throws NotAuthorisedUserException {
        final Account account = userService.getAuthorizedUser().getAccount();
        RememberingLevel rememberingLevel =
                rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account, 1);
        if (userCardQueue.getRememberingLevel() != null) {
            rememberingLevel = userCardQueue.getRememberingLevel();
        }

        if (status == UserCardQueueStatus.BAD && rememberingLevel.getOrderNumber() > 1) {
            userCardQueue.setRememberingLevel(rememberingLevelRepository
                    .findRememberingLevelByAccountEqualsAndOrderNumber(account, rememberingLevel.getOrderNumber() - 1));
        } else if (status == UserCardQueueStatus.GOOD &&
                rememberingLevel.getOrderNumber() < NUMBER_OF_REMEMBERING_LEVELS) {
            userCardQueue.setRememberingLevel(rememberingLevelRepository
                    .findRememberingLevelByAccountEqualsAndOrderNumber(account, rememberingLevel.getOrderNumber() + 1));
        } else {
            userCardQueue.setRememberingLevel(rememberingLevelRepository
                    .findRememberingLevelByAccountEqualsAndOrderNumber(account, rememberingLevel.getOrderNumber()));
        }
        userCardQueue.setDateToRepeat(new Date(userCardQueue.getCardDate().getTime() +
                userCardQueue.getRememberingLevel().getNumberOfPostponedDays() * DAY_IN_MILLISECONDS));
    }

    @Override
    public UserCardQueue getUserCardQueueById(long id) {
        return userCardQueueRepository.findOne(id);
    }

    @Override
    @Transactional
    public long countCardsThatNeedRepeating(Long deckId) throws NotAuthorisedUserException {
        return userCardQueueRepository.countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(
                userService.getAuthorizedUser().getId(), deckId, new Date());
    }
}
