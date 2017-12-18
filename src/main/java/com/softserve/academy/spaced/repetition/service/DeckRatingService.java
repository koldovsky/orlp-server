package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.UserStatusException;

public interface DeckRatingService {

    void addDeckRating(int rating, Long deckId) throws NotAuthorisedUserException, UserStatusException;

    DeckRating getDeckRatingById(Long id);
}
