package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;

import java.util.List;

/**
 *
 *
 */
public interface TestServiceToAddCards {
    /**
     * Adds a new deck
     *
     * @param cards list of cards which will be added to a new deck
     */
    void addDeck(List<Card> cards);
}
