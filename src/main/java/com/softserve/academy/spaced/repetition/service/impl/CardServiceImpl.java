package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.controller.utils.dto.CardFileDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.CardFileDTOList;
import com.softserve.academy.spaced.repetition.utils.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.*;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.*;
import java.util.*;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserCardQueueService userCardQueueService;
    @Autowired
    private DeckService deckService;

    @Override
    @Transactional
    public List<Card> getLearningCards(Long deckId) throws NotAuthorisedUserException {
        try {
            User user = userService.getAuthorizedUser();//1
            final int cardsNumber = accountService.getCardsNumber();//const
            List<Card> learningCards = new ArrayList<>();
            if (user.getAccount().getLearningRegime().equals(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING)) {
                learningCards = cardRepository.cardsForLearningWithOutStatus(user.getId(), deckId, cardsNumber);
                if (learningCards.size() < cardsNumber) {
                    learningCards.addAll(cardRepository.cardsQueueForLearningWithStatus(user.getId(), deckId,
                            cardsNumber - learningCards.size()));
                }
            } else if (user.getAccount().getLearningRegime()
                    .equals(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION)) {
                learningCards = cardRepository.getCardsThatNeedRepeating(deckId, new Date(), user.getId(), cardsNumber);
                if (learningCards.size() < cardsNumber) {
                    learningCards.addAll(cardRepository
                            .getNewCards(deckId, user.getId(), cardsNumber - learningCards.size()));
                }
            }
            return learningCards;
        } catch (NotAuthorisedUserException e) {
            return cardRepository.findAllByDeckId(deckId).subList(0, accountService.getCardsNumber());
        }
    }

    @Override
    public Card getCard(Long id) {
        return cardRepository.findOne(id);
    }

    @Override
    public void addCard(Card card, Long deckId) {
        if (card.getTitle().trim().isEmpty() || card.getAnswer().trim().isEmpty()
                || card.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("All of card fields must be filled");
        }
        Deck deck = deckRepository.findOne(deckId);
        card.setDeck(deck);
        deck.getCards().add(cardRepository.save(card));
    }

    @Override
    public void updateCard(Long id, Card card) {
        if (card.getTitle().trim().isEmpty() || card.getAnswer().trim().isEmpty()
                || card.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("All of card fields must be filled");
        }
        card.setId(id);
        card.setDeck(cardRepository.findOne(id).getDeck());
        cardRepository.save(card);
    }

    @Override
    public List<Card> getCardsQueue(long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        final int cardsNumber = accountService.getCardsNumber();
        List<Card> cardsQueue = cardRepository.cardsForLearningWithOutStatus(user.getId(), deckId, cardsNumber);

        if (cardsQueue.size() < cardsNumber) {
            cardsQueue.addAll(cardRepository.cardsQueueForLearningWithStatus(user.getId(), deckId, cardsNumber)
                    .subList(0, cardsNumber - cardsQueue.size()));
        }
        return cardsQueue;
    }


    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardRepository.deleteCardById(cardId);
    }

    @Override
    @Transactional
    public List<Card> getAdditionalLearningCards(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return cardRepository.getPostponedCards(deckId, new Date(), user.getId(), accountService.getCardsNumber());
    }

    @Override
    @Transactional
    public boolean areThereNotPostponedCardsAvailable(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        return userCardQueueService.countCardsThatNeedRepeating(deckId) > 0 ||
                !cardRepository.getNewCards(deckId, user.getId(), user.getAccount().getCardsNumber()).isEmpty();
    }

    @Override
    @Transactional
    public void uploadCards(MultipartFile cardsFile, Long deckId) throws WrongFormatException, EmptyFileException,
            NotOwnerOperationException, NotAuthorisedUserException, IOException {
        if (deckService.getDeckUser(deckId) != null) {
            if (!cardsFile.getContentType().equals("application/octet-stream")) {
                throw new WrongFormatException();
            } else if (cardsFile.isEmpty()) {
                throw new EmptyFileException("File is empty!");
            }
            Yaml yaml = new Yaml();
            InputStream in = cardsFile.getInputStream();
            try {
                CardFileDTOList cards = yaml.loadAs(in, CardFileDTOList.class);
                for (CardFileDTO card : cards.getCards()) {
                    addCard(new Card(card.getQuestion(), card.getAnswer(), card.getTitle()), deckId);
                }
            } catch (ParserException | ConstructorException ex) {
                throw new IllegalArgumentException("Invalid format of file!");
            }
        }
    }

    @Override
    public void downloadCards(Long deckId, OutputStream outputStream) {
        List<Map<String, String>> list = new ArrayList<>();
        cardRepository.findAllByDeckId(deckId).forEach(card -> {
            Map<String, String> cardMap = new HashMap<>();
            cardMap.put("title", card.getTitle());
            cardMap.put("question", card.getQuestion());
            cardMap.put("answer", card.getAnswer());
            list.add(cardMap);
        });
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
        options.setPrettyFlow(true);
        Map cardsMap = Collections.singletonMap("cards", list);
        Yaml yaml = new Yaml(options);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            yaml.dump(cardsMap, out);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Dumping of file failed!");
        }

    }

    @Override
    public void downloadCardsTemplate(OutputStream outputStream) {
        try (InputStream in = CardServiceImpl.class.getResourceAsStream("/data/CardsTemplate.yml")) {
            FileCopyUtils.copy(in, outputStream);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Copy of file failed!");
        }
    }
}
