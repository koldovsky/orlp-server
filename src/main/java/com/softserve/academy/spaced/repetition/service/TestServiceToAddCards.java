package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;

import java.util.List;

public interface TestServiceToAddCards {

    void addDeck(List<Card> cards);
}
