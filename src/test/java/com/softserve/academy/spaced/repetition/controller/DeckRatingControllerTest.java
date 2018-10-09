package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.service.DeckRatingService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class DeckRatingControllerTest {

    private MockMvc mockMvc;
    private final Long DECK_ID = 1L;
    private final Long DECK_RATING_ID = 1L;
    private final Integer RATING = 5;
    private final String ACCOUNT_EMAIL = "email@email";

    @InjectMocks
    private DeckRatingController deckRatingController;

    @Mock
    private DeckRatingService deckRatingService;
    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;
    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        final String MESSAGE_SOURCE_MESSAGE = "message";

        mockMvc = MockMvcBuilders.standaloneSetup(deckRatingController)
                .setControllerAdvice(exceptionHandlerController)
                .build();

        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
    }

    @Test
    public void getDeckRatingById() throws Exception {
        when(deckRatingService.getDeckRatingById(eq(DECK_RATING_ID))).thenReturn(createDeckRating());
        mockMvc.perform(get("/api/decks/{deckId}/ratings/{id}", DECK_ID, DECK_RATING_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"rating\":5,\"accountEmail\":\"email@email\",\"deckId\":1,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/decks/1/ratings/1\"}]}"));
        verify(deckRatingService, times(1)).getDeckRatingById(DECK_ID);
    }

    @Test
    public void testAddDeckRating() throws Exception {
        mockMvc.perform(post("/api/decks/{deckId}", DECK_ID)
                .content("{\"rating\":5,\"accountEmail\":\"email@email\",\"deck\":{\"id\":1}}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(deckRatingService, times(1)).addDeckRating(RATING, DECK_ID);
    }

    @Test
    public void testNotAuthorizedAddDeckRating() throws Exception {
        doThrow(NotAuthorisedUserException.class).when(deckRatingService).addDeckRating(RATING, DECK_ID);

        mockMvc.perform(post("/api/decks/{deckId}", DECK_ID)
                .content("{\"rating\":5,\"accountEmail\":\"email@email\",\"deckId\":1}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
        verify(deckRatingService, times(1)).addDeckRating(RATING, DECK_ID);
    }

    @Test
    public void testNegativeAddDeckRating() throws Exception {
        mockMvc.perform(post("/api/decks/{deckId}", DECK_ID)
                .content("{\"rating\":0}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyZeroInteractions(deckRatingService);

        mockMvc.perform(post("/api/decks/{deckId}", DECK_ID)
                .content("{\"rating\":6}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyZeroInteractions(deckRatingService);

        mockMvc.perform(post("/api/decks/{deckId}", DECK_ID)
                .content("{\"rating\":-1}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyZeroInteractions(deckRatingService);
    }

    private DeckRating createDeckRating() {
        Deck deck = new Deck();
        deck.setId(DECK_ID);
        DeckRating deckRating = new DeckRating(ACCOUNT_EMAIL, deck, RATING);
        deckRating.setId(DECK_RATING_ID);

        return deckRating;
    }
}
