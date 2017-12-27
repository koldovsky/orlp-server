package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.domain.UserCardQueueStatus;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;

public interface UserCardQueueService {
    
    void updateUserCardQueue(Long deckId, Long cardId, UserCardQueueStatus userCardQueueStatus)
            throws NotAuthorisedUserException;

    UserCardQueue getUserCardQueueById(long id);

    long countCardsThatNeedRepeating(Long deckId) throws NotAuthorisedUserException;

}
