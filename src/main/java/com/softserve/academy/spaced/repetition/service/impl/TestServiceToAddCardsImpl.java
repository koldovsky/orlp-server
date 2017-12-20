package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.TestServiceToAddCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestServiceToAddCardsImpl implements TestServiceToAddCards {

    @Autowired
    private DeckRepository deckRepository;

    @Override
    @Transactional
    public void addDeck(List<Card> cards) {
        Deck deck = deckRepository.getDeckById(1L);
        deck.setName("Collections");
        deck.setDescription("Collections are objects whose sole purpose is to store other objects, like arrays.");

        deck.setCards(cards);

        deckRepository.save(deck);
    }
}

