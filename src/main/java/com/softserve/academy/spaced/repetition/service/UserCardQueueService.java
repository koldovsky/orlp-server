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
     * @param cardId the card`s id which will be updated.
     * @param status the user`s status.
     * @throws NotAuthorisedUserException if unauthorized user sets updates the queue.
     * @throws IllegalArgumentException   if value of User Card Queue Status is not valid.
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
     * @param deckId the deck`s id in which the card`s will be updated repeated.
     * @return the quantity of cards which user needs to repeat.
     * @throws NotAuthorisedUserException if unauthorized user is tying to make this operation.
     */
    long countCardsThatNeedRepeating(Long deckId) throws NotAuthorisedUserException;
}
