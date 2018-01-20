package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.CardServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CardServiceTest {

    private final long CARD_ID = 1L;
    private final long DECK_ID = 1L;
    private final Integer CARDS_NUMBER = 10;
    private final Integer CARDS_NUMBER_FOR_SUBLIST = 11;
    private final long USER_ID = 1L;
    private final Long accountId = 1L;
    private final String password = "";
    private final String email = "user@email.com";
    private final boolean deactivated = true;
    private final Date lastPasswordResetDate = new Date();


    @InjectMocks
    private CardServiceImpl cardService;
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

    private User createMockedUser() {
        Authority authority = new Authority();
        Set<Authority> authorityLit = new HashSet<>();
        User mockedUser = new User(new Account(password,email,lastPasswordResetDate,authorityLit,AccountStatus.ACTIVE)
                , new Person("Ivan", "Kruk"), new Folder());
        mockedUser.setId(USER_ID);
        mockedUser.getAccount().setLearningRegime(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
        return mockedUser;
    }

    @Test
    public void testGetLearningCards() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(createMockedUser());
        when(accountService.getCardsNumber()).thenReturn(CARDS_NUMBER);
        when(cardRepository.cardsForLearningWithOutStatus(USER_ID, DECK_ID, CARDS_NUMBER))
                .thenReturn(DomainFactory.createLearningCards(DECK_ID));
        when(cardRepository.cardsQueueForLearningWithStatus(USER_ID, DECK_ID, CARDS_NUMBER_FOR_SUBLIST))
                .thenReturn(DomainFactory.createLearningCards(DECK_ID));
        when(cardRepository.getCardsThatNeedRepeating(DECK_ID, new Date(), USER_ID, CARDS_NUMBER))
                .thenReturn(DomainFactory.createLearningCards(DECK_ID));
        when(cardRepository.getNewCards(DECK_ID, USER_ID, CARDS_NUMBER))
                .thenReturn(DomainFactory.createLearningCards(DECK_ID));

        cardService.getLearningCards(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(accountService).getCardsNumber();
        verify(cardRepository).cardsForLearningWithOutStatus(USER_ID, DECK_ID, CARDS_NUMBER);
        verify(cardRepository).cardsQueueForLearningWithStatus(USER_ID, DECK_ID, CARDS_NUMBER_FOR_SUBLIST);
        verify(cardRepository).getCardsThatNeedRepeating(DECK_ID, new Date(), USER_ID, CARDS_NUMBER);
        verify(cardRepository).getNewCards(DECK_ID, USER_ID, CARDS_NUMBER);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void exceptionTestGetLearningCards() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        cardService.getLearningCards(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetCard() {
        when(cardRepository.findOne(CARD_ID)).thenReturn(any(Card.class));

        cardService.getCard(CARD_ID);
        verify(cardRepository).findOne(CARD_ID);
    }

    @Test
    public void testGetCardsQueue() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(createMockedUser());
        when(accountService.getCardsNumber()).thenReturn(CARDS_NUMBER);
        when(cardRepository.cardsQueueForLearningWithStatus(USER_ID, DECK_ID, CARDS_NUMBER))
                .thenReturn(DomainFactory.createLearningCards(DECK_ID));
        when(cardRepository.cardsForLearningWithOutStatus(USER_ID, DECK_ID, CARDS_NUMBER_FOR_SUBLIST))
                .thenReturn(DomainFactory.createLearningCards(DECK_ID));

        cardService.getCardsQueue(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(accountService).getCardsNumber();
        verify(cardRepository).cardsQueueForLearningWithStatus(USER_ID, DECK_ID, CARDS_NUMBER);
        verify(cardRepository).cardsForLearningWithOutStatus(USER_ID, DECK_ID, CARDS_NUMBER);


    }

    @Test(expected = NotAuthorisedUserException.class)
    public void exceptionTestGetCardsQueue() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        cardService.getCardsQueue(DECK_ID);
        verify(userService).getAuthorizedUser();
    }
}
