package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final static int CARDS_NUMBER = 10;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserService userService;

    public Card getCard(Long id) {
        return cardRepository.findOne(id);
    }

    public void addCard(Card card, Long deckId) {
        Deck deck = deckRepository.findOne(deckId);
        deck.getCards().add(cardRepository.save(card));
    }

    public void updateCard(Long id, Card card) {
        card.setId(id);
        cardRepository.save(card);
    }

    public List<Card> getCardsQueue(long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        List<Card> cardsQueue = cardRepository.cardsForLearningWithOutStatus(email, deckId, CARDS_NUMBER);

        if (cardsQueue.size() < CARDS_NUMBER) {
            cardsQueue.addAll(cardRepository.cardsQueueForLearningWithStatus(email, deckId, CARDS_NUMBER).subList(0, CARDS_NUMBER - cardsQueue.size()));
        }
        return cardsQueue;
    }

    public void deleteCard(Long id) {
        cardRepository.delete(id);
    }
}
