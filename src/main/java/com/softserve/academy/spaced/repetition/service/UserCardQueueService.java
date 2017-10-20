package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserCardQueueService {
    private static final int DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
    private final UserCardQueueRepository userCardQueueRepository;
    private final UserService userService;
    private final RememberingLevelRepository rememberingLevelRepository;

    @Autowired
    public UserCardQueueService(UserCardQueueRepository userCardQueueRepository, UserService userService, RememberingLevelRepository rememberingLevelRepository) {
        this.userCardQueueRepository = userCardQueueRepository;
        this.userService = userService;
        this.rememberingLevelRepository = rememberingLevelRepository;
    }

    @Transactional
    public void updateUserCardQueue(Long deckId, Long cardId, UserCardQueueStatus userCardQueueStatus)
            throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        UserCardQueue userCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(email, cardId);
        if (userCardQueue != null) {
            userCardQueue.setId(userCardQueue.getId());
        } else {
            userCardQueue = new UserCardQueue();
            userCardQueue.setCardId(cardId);
            userCardQueue.setDeckId(deckId);
            userCardQueue.setAccountEmail(email);
        }
        userCardQueue.setCardDate(new Date());

        if (user.getAccount().getLearningRegime() == LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING) {
            userCardQueue.setStatus(userCardQueueStatus);
        } else if (user.getAccount().getLearningRegime() == LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION) {
            applyCardsPostponingLearningRegime(userCardQueue, userCardQueueStatus);
        }
        userCardQueueRepository.save(userCardQueue);
    }

    private void applyCardsPostponingLearningRegime(UserCardQueue userCardQueue, UserCardQueueStatus status) {
        RememberingLevel rememberingLevel = rememberingLevelRepository.findOne(1L);
        if (userCardQueue.getRememberingLevel() != null) {
            rememberingLevel = userCardQueue.getRememberingLevel();
        }

        if (status == UserCardQueueStatus.BAD && rememberingLevel.getId() > 1) {
            userCardQueue.setRememberingLevel(rememberingLevelRepository.findOne(rememberingLevel.getId() - 1));
        } else if (status == UserCardQueueStatus.GOOD &&
                rememberingLevel.getId() < rememberingLevelRepository.count()) {
            userCardQueue.setRememberingLevel(rememberingLevelRepository.findOne(rememberingLevel.getId() + 1));
        } else {
            userCardQueue.setRememberingLevel(rememberingLevelRepository.findOne(rememberingLevel.getId()));
        }
        userCardQueue.setDateToRepeat(new Date(userCardQueue.getCardDate().getTime() +
                userCardQueue.getRememberingLevel().getNumberOfPostponedDays() * DAY_IN_MILLISECONDS));
    }

    public UserCardQueue getUserCardQueueById(long id) {
        return userCardQueueRepository.findOne(id);
    }
}
