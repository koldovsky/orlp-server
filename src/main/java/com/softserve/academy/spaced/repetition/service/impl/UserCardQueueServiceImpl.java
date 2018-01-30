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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

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

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    @Transactional
    public void updateUserCardQueue(Long deckId, Long cardId, String status)
            throws NotAuthorisedUserException, IllegalArgumentException {

        boolean userCardQueueStatusFound = Arrays.stream(UserCardQueueStatus.values())
                .anyMatch(UserCardQueueStatus.valueOf(status)::equals);

        if(!userCardQueueStatusFound) {
            throw new IllegalArgumentException(messageSource.getMessage("message.exception.userCardQueueStatusNotValid",
                    new Object[]{status}, locale));
        }

        UserCardQueueStatus userCardQueueStatus = UserCardQueueStatus.valueOf(status);

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
