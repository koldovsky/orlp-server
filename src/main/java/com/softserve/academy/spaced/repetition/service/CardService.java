package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by askol on 6/30/2017.
 */
@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Collection<Card> getAllCards(Long id) {
       // Collection<Card> cards = new ArrayList<>();
        return cardRepository.getAllCardsByDeckId(id);//.forEach(cards::add);
       // return cards;
    }

    public Card getCard(Long id) {
        return cardRepository.findOne(id);
    }

    public void addCard(Card card) {
        cardRepository.save(card);
    }

    public void updateCard(Long id, Card card) {
        cardRepository.save(card);
    }

    public void deleteCard(Long id) {
        cardRepository.delete(id);
    }
}
