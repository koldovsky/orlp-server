package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DeckDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;

public class DeckDTOImpl extends Deck implements DeckDTO {

    public DeckDTOImpl(Deck deck) {
        super(deck.getId(), deck.getName(), deck.getDescription(), deck.getCategory(), deck.getDeckOwner(), deck.getCards());
    }
}
