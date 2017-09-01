package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationHome;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    final static private int NUMB_OF_LEARNING_CARDS = 10;

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

    public List <Card> getCardsQueue(long deckId) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        List <Card> cardsQueue = new ArrayList <>();
        cardsQueue.addAll(getCardsForLearningWithOutStatus(username, deckId));

        if (cardsQueue.size() < NUMB_OF_LEARNING_CARDS) {
            cardsQueue.addAll(getCardsQueueForLearningWithStatus(username, deckId).subList(0, NUMB_OF_LEARNING_CARDS - cardsQueue.size()));
        }
        return cardsQueue;
    }

    public List <Card> getCardsQueueForLearningWithStatus(String accountEmail, long deckId) {
        return cardRepository.CardsQueueForLearningWithStatus(accountEmail, deckId, NUMB_OF_LEARNING_CARDS);
    }

    public List <Card> getCardsForLearningWithOutStatus(String accountEmail, long deckId) {
        return cardRepository.CardsForLearningWithOutStatus(accountEmail, deckId, NUMB_OF_LEARNING_CARDS);
    }

    @Transactional
    public void deleteCard(Long id) {
        cardRepository.delete(id);
    }
}
