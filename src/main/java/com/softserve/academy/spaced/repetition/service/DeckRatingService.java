package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;

/**
 * This interface works with the decks rating.
 * Only authorized users can rate the decks.
 */
public interface DeckRatingService {
    /**
     * Adds decks rating and checks if the user who rates deck is authorised and active.
     *
     * @param rating value that user sets for deck, must not be more than 5 and less than 1.
     * @param deckId deck`s id for which rating is set, must not be {@literal null}.
     * @throws NotAuthorisedUserException if unauthorized user writes comments.
     * @throws UserStatusException        if users status prevents to rate decks.
     */
    void addDeckRating(int rating, Long deckId) throws NotAuthorisedUserException, UserStatusException;

    /**
     * Gets deck`s rating from entity
     *
     * @param id rating`s id
     * @return deck`s rating
     */
    DeckRating getDeckRatingById(Long id);
}
