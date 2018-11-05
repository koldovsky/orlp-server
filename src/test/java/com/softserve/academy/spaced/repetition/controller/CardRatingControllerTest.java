package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.service.CardRatingService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class CardRatingControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CardRatingController cardRatingController;

    @Mock
    private CardRatingService cardRatingService;

    private static final long CARD_ID = 1L;
    private static final long CARD_RATING_ID = 1L;
    private static final int CARD_RATING = 2;
    private static final String ACCOUNT_EMAIL = "admin@gmail.com";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardRatingController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void testGetCardRatingById() throws Exception {
        when(cardRatingService.getCardRatingById(eq(CARD_ID))).thenReturn(createCardRating());
        mockMvc.perform(get("/api/decks/cards/{cardId}/rate", CARD_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating", Matchers.is(2)))
                .andExpect(jsonPath("$.accountEmail", Matchers.is("admin@gmail.com")))
                .andExpect(jsonPath("$.cardId", Matchers.is(1)));
        verify(cardRatingService, times(1)).getCardRatingById(CARD_ID);
    }

    @Test
    public void testAddCardRating() throws Exception {
        CardRating cardRating = createCardRating();
        doNothing().when(cardRatingService).addCardRating(cardRating, CARD_ID);
        mockMvc.perform(post("/api/decks/cards/{cardId}/rate", CARD_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(cardRating))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating", Matchers.is(2)))
                .andExpect(jsonPath("$.accountEmail", Matchers.is("admin@gmail.com")))
                .andExpect(jsonPath("$.cardId", Matchers.is(1)));
        verify(cardRatingService, times(1)).addCardRating(refEq(cardRating), eq(CARD_ID));
    }

    private CardRating createCardRating() {
        Card card = new Card();
        card.setId(CARD_ID);
        CardRating cardRating = new CardRating();
        cardRating.setId(CARD_RATING_ID);
        cardRating.setAccountEmail(ACCOUNT_EMAIL);
        cardRating.setCard(card);
        cardRating.setRating(CARD_RATING);
        return cardRating;
    }

}
