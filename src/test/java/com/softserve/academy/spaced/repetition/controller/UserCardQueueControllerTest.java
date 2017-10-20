package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.UserCardQueueStatus;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserCardQueueControllerTest {

    private static final long DECK_ID = 1L;
    private static final long CARD_ID = 1L;
    private MockMvc mockMvc;
    private UserCardQueueStatus status;

    @InjectMocks
    private UserCardQueueController userCardQueueController;

    @Mock
    private UserCardQueueService userCardQueueService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userCardQueueController)
                .setControllerAdvice(new ExceptionHandlerController())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void testSetStatusGood() throws Exception {
        mockMvc.perform(put("/api/private/decks/{deckId}/cards/{cardId}/queue", DECK_ID, CARD_ID)
                .content("\"GOOD\"")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userCardQueueService).updateUserCardQueue(DECK_ID, CARD_ID, UserCardQueueStatus.GOOD);
    }

    @Test
    public void testSetStatusIncorrect() throws Exception {
        mockMvc.perform(put("/api/private/decks/{deckId}/cards/{cardId}/queue", DECK_ID, CARD_ID)
                .content("\"Incorrect\"")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserCardQueueNotAuthorizedUserException() throws Exception {
        doThrow(NotAuthorisedUserException.class).when(userCardQueueService).updateUserCardQueue(eq(DECK_ID),
                eq(CARD_ID), any(UserCardQueueStatus.class));
        mockMvc.perform(put("/api/private/decks/{deckId}/cards/{cardId}/queue", DECK_ID, CARD_ID)
                .content("\"GOOD\"")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
