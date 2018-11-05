package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.ReplyToCommentDTO;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.service.DeckCommentService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class DeckCommentControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private DeckCommentController deckCommentController;

    @Mock
    private DeckCommentService commentService;

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;
    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        final String MESSAGE_SOURCE_MESSAGE = "message";

        mockMvc = MockMvcBuilders.standaloneSetup(deckCommentController)
                .setControllerAdvice(exceptionHandlerController)
                .build();

        Mockito.when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
    }

    @Test
    public void testGetCommentById() throws Exception {
        when(commentService.getCommentById(1L)).thenReturn(createTestComment());
        mockMvc.perform(get("/api/decks/{deckId}/comments/{commentId}", 1L, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentText", Matchers.is("testComment")))
                .andExpect(jsonPath("$.commentId", Matchers.is(1)));
    }

    private DeckComment createTestComment(){
        DeckComment comment = new DeckComment("testComment", new Date());
        Deck deck = new Deck();
        deck.setId(1L);
        deck.setName("testDeck");
        comment.setDeck(deck);
        comment.setCreatedBy(1L);
        comment.setId(1L);
        comment.setParentCommentId(null);
        comment.setPerson(new Person());
        return comment;
    }

    @Test
    public void testAddCommentForDeck() throws Exception{
        ReplyToCommentDTO rtcDTO = new ReplyToCommentDTO();
        rtcDTO.setCommentText("testComment");
        rtcDTO.setParentCommentId(0L);
        DeckComment comment = createTestComment();
        when(commentService.addCommentForDeck(1L, "testComment", 0L)).thenReturn(comment);
        mockMvc.perform(post("/api/decks/{deckId}/comments", 1L)
                .content(new ObjectMapper().writeValueAsString(rtcDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteComment()throws Exception{
        doNothing().when(commentService).deleteCommentById(anyLong());
        mockMvc.perform(delete("/api/decks/{deckId}/comments/{commentId}", 1L, 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllCommentsForDeck() throws Exception{
        List<Comment> listOfComments = new ArrayList<>();
        listOfComments.add(createTestComment());
        when(commentService.getAllCommentsForDeck(1L)).thenReturn(listOfComments);
        mockMvc.perform(get("/api/decks/{deckId}/comments", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].commentText", Matchers.is("testComment")))
                .andExpect(jsonPath("$.[0].commentId", Matchers.is(1)));
    }
}