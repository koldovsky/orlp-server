package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public UserCardQueue updateUserCardQueue(Long deckId, Long cardId, UserCardQueue userCardQueue)
            throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        UserCardQueue userCardQueueServer = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(email, cardId);
        RememberingLevel rememberingLevel = rememberingLevelRepository.findOne(1L);
        if (userCardQueueServer != null) {
            userCardQueue.setId(userCardQueueServer.getId());
            if (userCardQueueServer.getRememberingLevel() != null) {
                rememberingLevel = userCardQueueServer.getRememberingLevel();
            }
        }
        userCardQueue.setRememberingLevel(rememberingLevel);
        userCardQueue.setCardId(cardId);
        userCardQueue.setDeckId(deckId);
        userCardQueue.setAccountEmail(email);
        userCardQueue.setCardDate(new Date());

        if (user.getAccount().getLearningRegime().equals(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION)) {
            applyCardsPostponingLearningRegime(userCardQueue, rememberingLevel);
        }
        return userCardQueueRepository.save(userCardQueue);
    }

    private void applyCardsPostponingLearningRegime(UserCardQueue userCardQueue, RememberingLevel rememberingLevel) {
        if (userCardQueue.getStatus().equals(UserCardQueueStatus.BAD) && rememberingLevel.getId() > 1) {
            userCardQueue.setRememberingLevel(rememberingLevelRepository.findOne(rememberingLevel.getId() - 1));
        } else if (userCardQueue.getStatus().equals(UserCardQueueStatus.GOOD) &&
                rememberingLevel.getId() < rememberingLevelRepository.count()) {
            userCardQueue.setRememberingLevel(rememberingLevelRepository.findOne(rememberingLevel.getId() + 1));
        }
        userCardQueue.setStatus(null);
        userCardQueue.setDateToRepeat(new Date(userCardQueue.getCardDate().getTime() +
                userCardQueue.getRememberingLevel().getNumberOfPostponedDays() * DAY_IN_MILLISECONDS));
    }

    public UserCardQueue getUserCardQueueById(long id) {
        return userCardQueueRepository.findOne(id);
    }
}
