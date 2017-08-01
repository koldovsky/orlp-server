package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestServiceToAddCards {

    @Autowired
    private DeckRepository deckRepository;

    @Transactional
    public void addDeck(List<Card> cards) {
        Deck deck = deckRepository.getDeckById(1L);
        deck.setName("Collections");
        deck.setDescription("Collections are objects whose sole purpose is to store other objects, like arrays.");

        deck.setCards(cards);

        deckRepository.save(deck);
    }
}
