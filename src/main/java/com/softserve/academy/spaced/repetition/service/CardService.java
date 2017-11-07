package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    private final DeckRepository deckRepository;

    private final UserService userService;

    private final AccountService accountService;

    @Autowired
    public CardService(CardRepository cardRepository, DeckRepository deckRepository, AccountService accountService,
                       UserService userService) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
        this.userService = userService;
        this.accountService = accountService;
    }

    public List<Card> getLearningCards(Long deckId) throws NotAuthorisedUserException {
        try {
            User user = userService.getAuthorizedUser();
            final int cardsNumber = accountService.getCardsNumber();
            List<Card> learningCards = new ArrayList<>();
            if (user.getAccount().getLearningRegime().equals(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING)) {
                learningCards = cardRepository.cardsForLearningWithOutStatus(user.getId(), deckId, cardsNumber);
                if (learningCards.size() < cardsNumber) {
                    learningCards.addAll(cardRepository.cardsQueueForLearningWithStatus(user.getId(), deckId,
                            cardsNumber - learningCards.size()));
                }
            } else if (user.getAccount().getLearningRegime().equals(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION)) {
                learningCards = cardRepository.getCardsThatNeedRepeating(deckId, new Date(), user.getId(), cardsNumber);
                if (learningCards.size() < cardsNumber) {
                    learningCards.addAll(cardRepository.getNewCards(deckId, user.getId(), cardsNumber - learningCards.size()));
                }
            }
            return learningCards;
        } catch (NotAuthorisedUserException e) {
            return cardRepository.findAllByDeckId(deckId).subList(0, accountService.getCardsNumber());
        }
    }

    public Card getCard(Long id) {
        return cardRepository.findOne(id);
    }

    public void addCard(Card card, Long deckId) {
        if (card.getTitle().trim().isEmpty() || card.getAnswer().trim().isEmpty() || card.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("All of card fields must be filled");
        }
        Deck deck = deckRepository.findOne(deckId);
        card.setDeck(deck);
        deck.getCards().add(cardRepository.save(card));
    }

    public void updateCard(Long id, Card card) {
        if (card.getTitle().trim().isEmpty() || card.getAnswer().trim().isEmpty() || card.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("All of card fields must be filled");
        }
        card.setId(id);
        card.setDeck(cardRepository.findOne(id).getDeck());
        cardRepository.save(card);
    }

    public List<Card> getCardsQueue(long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        final int cardsNumber = accountService.getCardsNumber();
        List<Card> cardsQueue = cardRepository.cardsForLearningWithOutStatus(user.getId(), deckId, cardsNumber);

        if (cardsQueue.size() < cardsNumber) {
            cardsQueue.addAll(cardRepository.cardsQueueForLearningWithStatus(user.getId(), deckId, cardsNumber).subList(0,
                    cardsNumber - cardsQueue.size()));
        }
        return cardsQueue;
    }


    @Transactional
    public void deleteCard(Long cardId) {
        cardRepository.deleteCardById(cardId);
    }

    public List<Card> getAdditionalLearningCards(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return cardRepository.getPostponedCards(deckId, new Date(), user.getId(), accountService.getCardsNumber());
    }
}
