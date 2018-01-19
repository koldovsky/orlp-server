package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

/**
 * This interface works with cards rating.
 */
public interface CardRatingService {
    /**
     * Adds rating to card with the given identifier.
     *
     * @param cardRating value of rating with additional information.
     * @param cardId     must not be {@literal null}.
     * @throws NotAuthorisedUserException if user is not authorised
     */
    void addCardRating(CardRating cardRating, Long cardId) throws NotAuthorisedUserException;

    /**
     * Gets rating of card with the given identifier.
     *
     * @param cardId must not be {@literal null}.
     * @return cardRating with the given identifier
     */
    CardRating getCardRatingById(Long cardId);
}
