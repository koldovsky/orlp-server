package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.service.AccountService;
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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileLearningConrollerTest {

    @InjectMocks
    UserProfileLearningController controller;

    @Mock
    AccountService accountService;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void testGetLearningDetails() throws Exception{
        when(accountService.getAccountDetails()).thenReturn(getAcc());
        mockMvc.perform(get("/api/profile/learning-details")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardsNumber", Matchers.is(10)));
    }

    @Test
    public void testUpdateLearningDetails() throws Exception {
        Account acc = getAcc();
        when(accountService.updateAccountDetails(acc)).thenReturn(acc);
        mockMvc.perform(put("/api/profile/learning-details")
                .content(new ObjectMapper().writeValueAsString(acc))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardsNumber", Matchers.is(10)));
        verify(accountService,times(1)).updateAccountDetails(acc);
    }

    private Account getAcc() {
        Account acc = new Account();
        acc.setLearningRegime(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
        acc.setCardsNumber(10);
        acc.setRememberingLevels(new ArrayList<RememberingLevel>());
        return acc;
    }

    private String objectToJSON(Object obj) {
        StringWriter stringEmp = new StringWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(stringEmp,obj);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringEmp.toString();
    }
}
