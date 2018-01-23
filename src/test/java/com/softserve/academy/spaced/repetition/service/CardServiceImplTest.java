package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.CardServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CardServiceImplTest {

    private final Long USER_ID = 1L;
    private final Long DECK_ID = 1L;
    private final Long CARD_ID = 1L;
    private final Long ACCOUNT_ID = 1L;
    private final Integer ACCOUNT_CARDS_NUMBER = 10;
    private final long NOT_OWNER_USER_ID = 42L;
    private final long IMAGE_ID = 1L;
    private final String IMAGE_BASE_64 = "";
    private final String CONTENT_TYPE = "application/octet-stream";
    private final Long IMAGE_SIZE = 1L;
    private final String ENCODED_FILE_CONTENT = "content";
    private final Long MAX_FILE_SIZE = 1_048_576L;
    private final Long USER_QUOTA = 10_485_760L;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private UserService userService;
    @Mock
    private AccountService accountService;
    @Mock
    private UserCardQueueService userCardQueueService;
    @Mock
    private DeckService deckService;
    @Mock
    private MultipartFile cardsFile;
    @Mock
    private Yaml yaml;
    @Mock
    private InputStream inputStream;
    @Mock
    private OutputStream outputStream;
    @InjectMocks
    private CardServiceImpl cardService;
    private Deck deck;
    private User user;
    private Account account;
    private Card card;

    @Before
    public void setUp() throws Exception {
        account = DomainFactory.createAccount(ACCOUNT_ID, null, null, null, null
                , false, null, null, LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING
                , ACCOUNT_CARDS_NUMBER, null);
        user = DomainFactory.createUser(USER_ID, account, new Person(), new Folder(), null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null,
                1.0, null, new ArrayList<>(), null, null, null, null);
        card = DomainFactory.createCard(CARD_ID, "Title", "Question", "Answer", new Deck());

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(cardRepository.findOne(CARD_ID)).thenReturn(card);


    }

    @Test
    public void getLearningCardsWithLearningRegimeBadNormalGood() throws NotAuthorisedUserException {
        List<Card> learningCards = new ArrayList<>();
        when(accountService.getCardsNumber()).thenReturn(ACCOUNT_CARDS_NUMBER);
        when(cardRepository.cardsForLearningWithOutStatus(user.getId(), DECK_ID, ACCOUNT_CARDS_NUMBER)).thenReturn(learningCards);
        when(cardRepository.cardsQueueForLearningWithStatus(user.getId(), DECK_ID, ACCOUNT_CARDS_NUMBER - learningCards.size()))
                .thenReturn(learningCards);

        List<Card> result = cardService.getLearningCards(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(accountService).getCardsNumber();
        verify(cardRepository).cardsForLearningWithOutStatus(user.getId(), DECK_ID, ACCOUNT_CARDS_NUMBER);
        verify(cardRepository).cardsQueueForLearningWithStatus(user.getId()
                , DECK_ID, ACCOUNT_CARDS_NUMBER - learningCards.size());
        assertNotNull(result);
    }

    @Test
    public void getLearningCardsWithLearningRegimeCardPositionUsingSpaced() throws NotAuthorisedUserException {
        List<Card> learningCards = new ArrayList<>();
        user.getAccount().setLearningRegime(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(cardRepository.getCardsThatNeedRepeating(eq(DECK_ID), any(Date.class), eq(user.getId()), eq(0)))
                .thenReturn(learningCards);

        List<Card> result = cardService.getLearningCards(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(accountService).getCardsNumber();
        verify(cardRepository).getCardsThatNeedRepeating(eq(DECK_ID),
                any(Date.class), eq(user.getId()), eq(0));
        assertNotNull(result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void getLearningCardsByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

//        when(cardRepository.findAllByDeckId(DECK_ID)).thenReturn(new ArrayList<>());
//        when(accountService.getCardsNumber()).thenReturn(0);

//        List<Card> result =
        cardService.getLearningCards(DECK_ID);
        verify(userService).getAuthorizedUser();
//        verify(cardRepository).findAllByDeckId(DECK_ID);
//        verify(accountService).getCardsNumber();
//        assertNotNull(result);
    }

    @Test
    public void getCard() {
        Card result = cardService.getCard(CARD_ID);
        verify(cardRepository).findOne(CARD_ID);
        assertEquals(card, result);
    }

    @Test
    public void addCard() {
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(cardRepository.save(card)).thenReturn(card);

        cardService.addCard(card, DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(cardRepository).save(card);
    }

    @Test
    public void updateCard() {
        when(cardRepository.save(card)).thenReturn(card);

        cardService.updateCard(CARD_ID, card);
        verify(cardRepository).findOne(DECK_ID);
        verify(cardRepository).save(card);
    }

    @Test
    public void getCardsQueue() throws NotAuthorisedUserException {
        List<Card> learningCards = new ArrayList<>();
        List<Card> learningCardsTemp = new ArrayList<>();
        learningCardsTemp.add(card);
        learningCardsTemp.add(card);
        when(accountService.getCardsNumber()).thenReturn(1);
        when(cardRepository.cardsForLearningWithOutStatus(user.getId(), DECK_ID, 11)).thenReturn(learningCardsTemp);
//        when(cardRepository.getCardsThatNeedRepeating(eq(DECK_ID), any(Date.class), eq(user.getId()), eq(0)))
//                .thenReturn(learningCards);

        cardService.getCardsQueue(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(accountService).getCardsNumber();
        verify(cardRepository).cardsForLearningWithOutStatus(user.getId(), DECK_ID, 1);
//        verify(cardRepository).getCardsThatNeedRepeating(eq(DECK_ID),
//                any(Date.class), eq(user.getId()), eq(0));
    }

    @Test
    public void deleteCard() {
        doNothing().when(cardRepository).deleteCardById(CARD_ID);

        cardService.deleteCard(CARD_ID);
    }

    @Test
    public void getAdditionalLearningCards() throws NotAuthorisedUserException {
        List<Card> learningCards = new ArrayList<>();
        when(accountService.getCardsNumber()).thenReturn(ACCOUNT_CARDS_NUMBER);
        when(cardRepository.getPostponedCards(DECK_ID, new Date(), user.getId(), ACCOUNT_CARDS_NUMBER))
                .thenReturn(learningCards);

        cardService.getAdditionalLearningCards(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(accountService).getCardsNumber();
        verify(cardRepository).getPostponedCards(eq(DECK_ID), any(Date.class), eq(USER_ID), eq(ACCOUNT_CARDS_NUMBER));
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void getAdditionalLearningCardsByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        cardService.getAdditionalLearningCards(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void areThereNotPostponedCardsAvailable() throws NotAuthorisedUserException {
        List<Card> learningCards = new ArrayList<>();
        learningCards.add(card);
        when(userCardQueueService.countCardsThatNeedRepeating(DECK_ID)).thenReturn(0L);
        when(cardRepository.getNewCards(DECK_ID, user.getId(), user.getAccount().getCardsNumber()))
                .thenReturn(learningCards);

        boolean result = cardService.areThereNotPostponedCardsAvailable(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueService).countCardsThatNeedRepeating(DECK_ID);
        verify(cardRepository).getNewCards(DECK_ID, user.getId(), user.getAccount().getCardsNumber());
        assertEquals(true, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void areThereNotPostponedCardsAvailableByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        cardService.areThereNotPostponedCardsAvailable(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void uploadCards() throws WrongFormatException, EmptyFileException, NotOwnerOperationException,
            NotAuthorisedUserException, IOException {


        when(cardsFile.getContentType()).thenReturn(CONTENT_TYPE);
        when(cardsFile.getInputStream()).thenReturn(inputStream);
//        when(yaml.loadAs(any(InputStream.class), eq(CardFileDTOList.class))).thenReturn(asd);
//                doNothing().when(yaml).dump(any(Map.class), any());


        when(deckService.getDeckUser(DECK_ID)).thenReturn(deck);
        cardService.uploadCards(cardsFile, DECK_ID);
        verify(deckService).getDeckUser(DECK_ID);
    }

    @Test
    public void downloadCards() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        when(cardRepository.findAllByDeckId(DECK_ID)).thenReturn(cardList);

        cardService.downloadCards(DECK_ID, outputStream);
        verify(cardRepository).findAllByDeckId(DECK_ID);
    }

    @Test
    public void downloadCardsTemplate() {
        cardService.downloadCardsTemplate(outputStream);
    }
}