package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;



@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Transactional
    public Card getCard(Long id) {
        return cardRepository.findOne(id);
    }

    @Transactional
    public void addCard(Card card, Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.getCards().add(cardRepository.save(card));
    }

    @Transactional
    public void updateCard(Long id, Card card) {
        card.setId(id);
        cardRepository.save(card);
    }

    @Transactional
    public void deleteCard(Long id) {
        cardRepository.delete(id);
    }
}
