package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.FolderService;
import org.hamcrest.Matchers;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FolderControllerTest {

    private MockMvc mockMvc;
    private final Long DECK_ID_1 = 1L;
    private final Long DECK_ID_2 = 2L;
    private final Long FOLDER_ID = 1L;
    private final String DECK_DESCRIPTION = "Interview questions about Java";
    private final String DECK_NAME = "Java interview #1";
    private final String DECK_SYNTAX = "JAVA";
    private final Double DECK_RATING = 1.0;

    @InjectMocks
    private FolderController folderController;

    @Mock
    private FolderService folderService;

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        final String MESSAGE_SOURCE_MESSAGE = "message";

        mockMvc = MockMvcBuilders.standaloneSetup(folderController)
                .setControllerAdvice(exceptionHandlerController)
                .build();

        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
    }

    @Test
    public void testAddDeckToFolder() throws Exception {
        when(folderService.addDeck(eq(DECK_ID_1))).thenReturn(createDeck());
        mockMvc.perform(put("/api/user/folder/add/deck/{deckId}", DECK_ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deckId", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Java interview #1")));
        verify(folderService, times(1)).addDeck(DECK_ID_1);
    }

    @Test
    public void testDeleteUserDeck() throws Exception {
        mockMvc.perform(delete("/api/user/folder/decks/{deckId}", DECK_ID_1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(folderService, times(1)).deleteDeck(DECK_ID_1);
    }

    @Test
    public void testGetAllDecksInFolder() throws Exception {
        List<Long> listWithFolderId = new ArrayList<>();
        listWithFolderId.add(DECK_ID_1);
        listWithFolderId.add(DECK_ID_2);
        when(folderService.getAllDecksIdWithFolder()).thenReturn(listWithFolderId);
        mockMvc.perform(get("/api/user/folder/decksId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[1, 2]"));
        verify(folderService, times(1)).getAllDecksIdWithFolder();
    }

    @Test
    public void testGetAllDecksWithFolder() throws Exception {
        List<Deck> decks = new ArrayList<>();
        decks.add(createDeck());
        when(folderService.getAllDecksByFolderId(eq(FOLDER_ID))).thenReturn(decks);
        mockMvc.perform(get("/api/user/folder/{folderId}/decks", FOLDER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", Matchers.is("Java interview #1")))
                .andExpect(jsonPath("$.[0].deckId", Matchers.is(1)));
        verify(folderService, times(1)).getAllDecksByFolderId(FOLDER_ID);
    }

    private Deck createDeck() {
        User user = new User();
        user.setId(1L);
        Deck deck = new Deck();
        deck.setName(DECK_NAME);
        deck.setHidden(false);
        deck.setDescription(DECK_DESCRIPTION);
        deck.setSyntaxToHighlight(DECK_SYNTAX);
        deck.setRating(DECK_RATING);
        deck.setId(DECK_ID_1);
        deck.setDeckOwner(user);
        return deck;
    }
}
