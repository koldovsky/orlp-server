package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

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
