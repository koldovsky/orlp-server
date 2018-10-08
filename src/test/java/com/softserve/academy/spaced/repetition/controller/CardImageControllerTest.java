package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.service.CardImageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CardImageControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CardImageController cardImageController;

    @Mock
    private CardImageService cardImageService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardImageController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void deleteCardImage() throws Exception {
        doNothing().when(cardImageService).deleteById(1L);
        mockMvc.perform(delete("/api/cardImage/{id}", 1L))
                .andExpect(status().isOk());
        verify(cardImageService, times(1)).deleteById(1L);
        verifyZeroInteractions(cardImageService);
    }
}
