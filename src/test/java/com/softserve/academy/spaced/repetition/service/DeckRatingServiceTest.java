package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.DeckRatingRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.DeckRatingServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class DeckRatingServiceTest {

    @InjectMocks
    private DeckRatingServiceImpl deckRatingService;

    @Mock
    private DeckRatingRepository deckRatingRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserService userService;

    private User user;
    private Account account;
    private Deck deck;
    private DeckRating deckRating;

    private final long USER_ID = 1L;
    private final long ACCOUNT_ID = 1L;
    private final String ACCOUNT_EMAIL = "account@test.com";
    private final long DECK_ID = 1L;
    private final long DECK_RATING_ID = 1L;
    private final int DECK_RATING_RATING = 1;

    @Before
    public void setUp() throws NotAuthorisedUserException {
        account = DomainFactory.createAccount(ACCOUNT_ID, null, ACCOUNT_EMAIL, null, null, false, null, null, null,
                null, null);
        user = DomainFactory.createUser(USER_ID, account, null, null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0D, user, null, null, null, null);
        deckRating = DomainFactory.createDeckRating(DECK_RATING_ID, ACCOUNT_EMAIL, deck, DECK_RATING_RATING);

        when(userService.getAuthorizedUser()).thenReturn(user);
    }

    @Test
    public void testAddDeckRating() throws NotAuthorisedUserException, UserStatusException {
        doNothing().when(userService).isUserStatusActive(user);
        when(deckRatingRepository.findAllByAccountEmailAndDeckId(ACCOUNT_EMAIL, DECK_ID))
                .thenReturn(deckRating);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(deckRatingRepository.save(deckRating)).thenReturn(deckRating);
        when(deckRatingRepository.findRatingByDeckId(DECK_ID)).thenReturn((double) DECK_RATING_RATING);
        when(deckRepository.save(deck)).thenReturn(deck);

        deckRatingService.addDeckRating(DECK_RATING_RATING, DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(userService).isUserStatusActive(user);
        verify(deckRatingRepository).findAllByAccountEmailAndDeckId(ACCOUNT_EMAIL, DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(deckRatingRepository).save(deckRating);
        verify(deckRatingRepository).findRatingByDeckId(DECK_ID);
        verify(deckRepository).save(deck);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddDeckRatingByNotAuthorisedUser() throws NotAuthorisedUserException, UserStatusException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        deckRatingService.addDeckRating(DECK_RATING_RATING, DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = UserStatusException.class)
    public void testAddDeckRatingByUserWithNotActiveStatus() throws NotAuthorisedUserException, UserStatusException {
        doThrow(UserStatusException.class).when(userService).isUserStatusActive(user);

        deckRatingService.addDeckRating(DECK_RATING_RATING, DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(userService).isUserStatusActive(user);
    }

    @Test
    public void testGetDeckRatingById() {
        when(deckRatingRepository.findOne(DECK_ID)).thenReturn(deckRating);

        DeckRating result = deckRatingService.getDeckRatingById(DECK_ID);
        verify(deckRatingRepository).findOne(DECK_ID);
        assertEquals(deckRating, result);
    }
}
