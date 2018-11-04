package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class CardsFileControllerTest {

    private static final long DECK_ID = 1;

    private MockMvc mockMvc;

    @InjectMocks
    private CardsFileController cardsFileController;

    @Mock
    private CardService cardService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardsFileController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = createMockMultipartFile();
        doNothing().when(cardService).uploadCards(file, DECK_ID);
        mockMvc.perform(fileUpload("/api/upload/deck/" + DECK_ID + "/cards")
                .file(createMockMultipartFile())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(cardService, times(1)).uploadCards(refEq(file), eq(DECK_ID));
    }

    @Test
    public void testDownloadFile() throws Exception {
        doNothing().when(cardService).downloadCards(DECK_ID, createHttpServletResponse());
        mockMvc.perform(get("/api/download/deck/{deckId}/cards", DECK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=Cards.yml"))
                .andExpect(header().string("content-type", "application/octet-stream"));
        verify(cardService, times(1)).downloadCards(anyLong(), any(OutputStream.class));
    }

    @Test
    public void testDownloadFileTemplate() throws Exception {
        doNothing().when(cardService).downloadCards(DECK_ID, createTemplateHttpServletResponse());
        mockMvc.perform(get("/api/download/template/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=CardsTemplate.yml"))
                .andExpect(header().string("content-type", "application/octet-stream"));
        verify(cardService, times(1)).downloadCardsTemplate(any(OutputStream.class));
    }

    private MockMultipartFile createMockMultipartFile() throws IOException {
        return new MockMultipartFile(
                "file",
                "CardsTemplate.yml",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream("src/main/resources/data/CardsTemplate.yml"));
    }

    private OutputStream createHttpServletResponse() throws IOException {
        HttpServletResponse mockResponse = new MockHttpServletResponse();
        mockResponse.setContentType("application/octet-stream");
        mockResponse.addHeader("Content-Disposition", "attachment; filename=Cards.yml");
        mockResponse.setStatus(200);
        return mockResponse.getOutputStream();
    }

    private OutputStream createTemplateHttpServletResponse() throws IOException {
        HttpServletResponse mockResponse = new MockHttpServletResponse();
        mockResponse.setContentType("application/octet-stream");
        mockResponse.setHeader("Content-Disposition", "attachment; filename=CardsTemplate.yml");
        mockResponse.setStatus(200);
        return mockResponse.getOutputStream();
    }
}
