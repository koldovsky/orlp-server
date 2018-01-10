package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

/**
 * This interface works with user card queue.
 */
public interface UserCardQueueService {
    /**
     * Updates the user`s card queue.
     *
     * @param deckId the deck`s id in which the card`s queue will be updated.
     * @param cardId
     * @param status
     * @throws NotAuthorisedUserException
     * @throws IllegalArgumentException
     */
    void updateUserCardQueue(Long deckId, Long cardId, String status)
            throws NotAuthorisedUserException, IllegalArgumentException;

    /**
     * Gets user card queue with the given identifier.
     *
     * @param id the user`s card queue id.
     * @return the user card queue.
     */
    UserCardQueue getUserCardQueueById(long id);

    /**
     * Gets the quantity of cards which user needs to repeat.
     *
     * @param deckId
     * @return the quantity of cards which user needs to repeat.
     * @throws NotAuthorisedUserException
     */
    long countCardsThatNeedRepeating(Long deckId) throws NotAuthorisedUserException;
}
