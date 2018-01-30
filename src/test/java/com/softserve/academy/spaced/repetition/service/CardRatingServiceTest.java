package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.repository.CardRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.service.impl.CardRatingServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CardRatingServiceTest {

    private final Long CARD_ID = 1L;
    private final String EMAIL = "account@test.com";
    @Mock
    private CardRatingRepository cardRatingRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CardRatingServiceImpl cardRatingService;
    private User user;
    private Card card;
    private CardRating cardRating;

    @Before
    public void setUp() {
        Account account = DomainFactory.createAccount(1L, "", EMAIL, null,
                AccountStatus.ACTIVE, true, null, null, null,
                null, null);
        user = DomainFactory.createUser(1L, account, null, null, null);
        cardRating = DomainFactory.createCardRating(1L, EMAIL, card, 1);
        card = DomainFactory.createCard(CARD_ID, "Card 1", "How many access modifiers do you know in Java?",
                "There are 4 access modifiers in Java: public, protected, default and private", null);
    }

    @Test()
    public void testAddCardRating() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(cardRatingRepository.findCardRatingByAccountEmailAndCard_Id(EMAIL, CARD_ID))
                .thenReturn(cardRating);
        when(cardRepository.findOne(CARD_ID)).thenReturn(card);
        when(cardRatingRepository.save(cardRating)).thenReturn(cardRating);
        when(cardRatingRepository.findRatingByCard_Id(CARD_ID)).thenReturn(1.0);
        when(cardRepository.save(card)).thenReturn(card);

        cardRatingService.addCardRating(cardRating, CARD_ID);
        verify(userService).getAuthorizedUser();
        verify(cardRatingRepository).findCardRatingByAccountEmailAndCard_Id(EMAIL, CARD_ID);
        verify(cardRepository).findOne(CARD_ID);
        verify(cardRatingRepository).save(cardRating);
        verify(cardRatingRepository).findRatingByCard_Id(CARD_ID);
        verify(cardRepository).save(card);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddCardRatingByUnauthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        cardRatingService.addCardRating(cardRating, CARD_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetCardRatingById() {
        when(cardRatingRepository.findOne(CARD_ID)).thenReturn(cardRating);

        CardRating result = cardRatingService.getCardRatingById(CARD_ID);
        verify(cardRatingRepository).findOne(CARD_ID);
        assertEquals(cardRating, result);
    }
}
