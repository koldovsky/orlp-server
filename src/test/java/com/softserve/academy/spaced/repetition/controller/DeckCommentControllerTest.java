package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.service.DeckCommentService;
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

import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class DeckCommentControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private DeckCommentController deckCommentController;

    @Mock
    private DeckCommentService deckCommentService;

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
    public void getCommentById() throws Exception {

        //when(deckCommentService.addCommentForDeck(1L, "1", null)).thenReturn(comment);
        //MvcResult result =
        mockMvc.perform(post("api/decks/{deckId}/comments", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"commentText\":\"1\",\"parentCommentId\":null}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        //.andExpect(jsonPath("$*", hasSize(4)))
        //    .andReturn()
        ;
        //   System.out.println(result.getResponse().getContentAsString());
        //   System.out.println(result.getRequest().getRequestURL());


    }
}