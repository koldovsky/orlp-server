package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;

public interface CardRatingService {
    void addCardRating(CardRating cardRating, Long cardId) throws NotAuthorisedUserException;

    CardRating getCardRatingById(Long cardId);
}
