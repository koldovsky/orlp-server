package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;

    public List<Deck> getAllDecksByCategoryId(Long id) {
        return deckRepository.getAllDecksByCategoryId(id);
    }

    public Deck getDeck(Long id) {
        return deckRepository.findOne(id);
    }

    public void addDeck(Deck deck) {
        deckRepository.save(deck);
    }

    public void updateDeck(Long id, Deck deck) {
        deckRepository.save(deck);
    }

    public void deleteDeck(Long id) {
        deckRepository.delete(id);
    }
}
