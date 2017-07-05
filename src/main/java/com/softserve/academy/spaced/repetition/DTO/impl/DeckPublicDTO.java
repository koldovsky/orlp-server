package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DeckPublic;
import com.softserve.academy.spaced.repetition.domain.Deck;

public class DeckPublicDTO extends Deck implements DeckPublic {
    public DeckPublicDTO(Deck deck) {
        super(deck.getId(), deck.getName(), deck.getDescription(), deck.getCategory(), deck.getDeckOwner(), deck.getCards());
    }
}
