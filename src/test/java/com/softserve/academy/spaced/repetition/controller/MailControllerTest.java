package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.MailDTO;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.service.MailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MailControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private MailController mailController;

    @Mock
    private MailService mailService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mailController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void testFromContactUsFromToEmailSender() throws Exception {
        MailDTO mailDTO = createNewMailDTO();
        doNothing().when(mailService).sendRequestFromContactUsFormToEmail(mailDTO);
        mockMvc.perform(post("/api/contactus")
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mailDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mailService, times(1)).sendRequestFromContactUsFormToEmail(refEq(mailDTO));
    }

    private MailDTO createNewMailDTO() {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setEmail("test@test.com");
        mailDTO.setName("Tester");
        mailDTO.setSubject("TestSubject");
        mailDTO.setText("TestText");
        return mailDTO;
    }
}
