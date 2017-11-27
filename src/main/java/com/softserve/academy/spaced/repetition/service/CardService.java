package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.dto.CardFileDTO;
import com.softserve.academy.spaced.repetition.dto.CardFileDTOList;
import com.softserve.academy.spaced.repetition.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.exceptions.WrongFormatException;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;

    private final DeckRepository deckRepository;

    private final UserService userService;

    private final AccountService accountService;

    private final UserCardQueueService userCardQueueService;

    private final DeckService deckService;

    @Autowired
    public CardService(CardRepository cardRepository, DeckRepository deckRepository, AccountService accountService,
                       UserService userService, UserCardQueueService userCardQueueService, DeckService deckService) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
        this.userService = userService;
        this.accountService = accountService;
        this.userCardQueueService = userCardQueueService;
        this.deckService = deckService;
    }

    @Transactional
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

    @Transactional
    public List<Card> getAdditionalLearningCards(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return cardRepository.getPostponedCards(deckId, new Date(), user.getId(), accountService.getCardsNumber());
    }

    @Transactional
    public boolean areThereNotPostponedCardsAvailable(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return userCardQueueService.countCardsThatNeedRepeating(deckId) > 0 ||
                !cardRepository.getNewCards(deckId, user.getId(), user.getAccount().getCardsNumber()).isEmpty();
    }

    @Transactional
    public void uploadCards(MultipartFile cardsFile, Long deckId) throws WrongFormatException, EmptyFileException, IOException, NotOwnerOperationException, NotAuthorisedUserException {
        if (deckService.getDeckUser(deckId) != null) {
            if (!cardsFile.getContentType().equals("application/octet-stream")) {
                throw new WrongFormatException();
            } else if (cardsFile.isEmpty()) {
                throw new EmptyFileException();
            }
            Yaml yaml = new Yaml();
            InputStream in = cardsFile.getInputStream();
            CardFileDTOList cards = yaml.loadAs(in, CardFileDTOList.class);
            for (CardFileDTO card : cards.getCards()) {
                addCard(new Card(card.getQuestion(), card.getAnswer(), card.getTitle()), deckId);
            }
        }
    }

    public List<CardFileDTO> downloadCards(Long deckId) {
        return cardRepository.findAllByDeckId(deckId).stream().map(card ->
                new CardFileDTO(card.getTitle(), card.getQuestion(), card.getAnswer())).collect(Collectors.toList());
    }

}
