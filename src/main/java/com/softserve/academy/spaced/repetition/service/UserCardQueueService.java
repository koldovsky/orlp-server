package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

public interface UserCardQueueService {
    void updateUserCardQueue(Long deckId, Long cardId, String status)
            throws NotAuthorisedUserException, IllegalArgumentException;

    UserCardQueue getUserCardQueueById(long id);

    long countCardsThatNeedRepeating(Long deckId) throws NotAuthorisedUserException;
}
