package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;

    public List<Deck> getAllDecksByCategoryId(Long category_id) {
        return deckRepository.getAllDecksByCategoryId(category_id);
    }

    public Deck getDeck(Long deck_id) {
        return deckRepository.findOne(deck_id);
    }

    public List <Deck> findTop4ByOrderById() {
        return deckRepository.findTop4ByOrderById();
    }

    public List<Card> getAllCardsByDeckId(Long deck_id) {
        Deck deck = deckRepository.findOne(deck_id);
        return deck.getCards();
    }

    public void addDeck(Deck deck, Long category_id) {
        deck.setCategory(new Category(category_id));
        deckRepository.save(deck);
    }

    public void updateDeck(Deck deck, Long deck_id) {
        deck.setId(deck_id);
        deckRepository.save(deck);
    }

    public void deleteDeck(Long deck_id) {
        deckRepository.delete(deck_id);
    }
}
