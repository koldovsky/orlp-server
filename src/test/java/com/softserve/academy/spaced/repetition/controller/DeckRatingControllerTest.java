package com.softserve.academy.spaced.repetition.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.service.DeckRatingService;
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
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class DeckRatingControllerTest {

    private MockMvc mockMvc;

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
        when(deckRatingService.getDeckRatingById(eq(77L))).thenReturn(createDeckRating());
        mockMvc.perform(get("/api/deck/{deckId}/rating/{id}", 5L, 77L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"rating\":3,\"accountEmail\":\"email@email\",\"deckId\":5,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/deck/5/rating/77\"}]}"));
    }

    private DeckRating createDeckRating() {
        Deck deck = new Deck();
        deck.setId(5L);
        DeckRating deckRating = new DeckRating("email@email", deck, 3);
        deckRating.setId(77L);
        return deckRating;
    }


    @Test
    public void testAddDeckRating() throws Exception {
        mockMvc.perform(post("/api/private/deck/{deckId}", 5L)
                .content("{\"rating\":3,\"accountEmail\":\"email@email\",\"deck\":{\"id\":5}}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(deckRatingService, times(1)).addDeckRating(eq(3), eq(5L));
    }

    @Test
    public void testNotAuthorizedAddDeckRating() throws Exception {
        doThrow(NotAuthorisedUserException.class).when(deckRatingService).addDeckRating(eq(3), eq(5L));

        mockMvc.perform(post("/api/private/deck/{deckId}", 5L)
                .content("{\"rating\":3,\"accountEmail\":\"email@email\",\"deckId\":5}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testNegativeAddDeckRating() throws Exception {
        mockMvc.perform(post("/api/private/deck/{deckId}", 5L)
                .content("{\"rating\":0}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/private/deck/{deckId}", 5L)
                .content("{\"rating\":6}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}