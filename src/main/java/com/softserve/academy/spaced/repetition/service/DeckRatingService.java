package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;

/**
 * This interface works with the decks rating.
 * Only authorized users can rates the decks.
 */
public interface DeckRatingService {
    /**
     * Adds decks rating and checks is the user that rates deck is authorised and active.
     *
     * @param rating - value that user sets for deck, must not be over than 5 and less than 1.
     * @param deckId - decks id for which rating is set, must not be {@literal null}.
     * @throws NotAuthorisedUserException - if unauthorized user writes comments.
     * @throws UserStatusException        - if users status prevents to rate decks.
     */
    void addDeckRating(int rating, Long deckId) throws NotAuthorisedUserException, UserStatusException;

    /**
     *
     *
     *
     * a don`t know what does this method do, it`s your turn to investigate it
     *
     *
     *
     *
     *
     */
    DeckRating getDeckRatingById(Long id);
}
