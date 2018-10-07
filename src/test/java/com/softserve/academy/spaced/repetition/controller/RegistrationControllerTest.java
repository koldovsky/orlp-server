package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Authority;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.*;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.service.AccountVerificationByEmailService;
import com.softserve.academy.spaced.repetition.service.RegistrationService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;
    @Mock
    private AccountVerificationByEmailService accountVerificationByEmailService;
    @Mock
    private UserService userService;
    @Mock
    private AccountService accountService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();

    }

    private User createUser() {
        User user = new User();
        Account account = new Account();

        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName(AuthorityName.ROLE_USER);
        authorities.add(authority);

        account.setAuthorities(authorities);
        account.setEmail("helloworld@gmail.com");
        account.setPassword("12345676786");
        account.setId(1L);
        account.setStatus(AccountStatus.ACTIVE);
        account.setDeactivated(true);
        account.setAuthenticationType(AuthenticationType.LOCAL);
        account.setLearningRegime(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
        account.setCardsNumber(1);

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Admin");
        person.setLastName("Admin");
        person.setImageType(ImageType.LINK);
        person.setImageBase64("Image base 64");
        person.setImage("Image.jpg");

        user.setId(1L);
        user.setAccount(account);
        user.setPerson(person);

        return user;
    }


    @Test
    public void addUser() throws Exception {
        User user = createUser();

        when(registrationService.registerNewUser(user)).thenReturn(user);
        mockMvc.perform(post("/api/registration")
//       .content(new ObjectMapper().writeValueAsString(user))
//       .content("{\n" +
//                "  \"account\": {\n" +
//                "    \"authenticationType\": \"LOCAL\",\n" +
//                "    \"authorities\": [\n" +
//                "      {\n" +
//                "        \"id\": 1,\n" +
//                "        \"name\": \"ROLE_USER\"\n" +
//                "      }\n" +
//                "    ],\n" +
//                "    \"cardsNumber\": 1,\n" +
//                "    \"deactivated\": true,\n" +
//                "    \"email\": \"email@email.com\",\n" +
//                "    \"id\": 1,\n" +
//                "    \"lastPasswordResetDate\": \"2018-10-07T10:39:13.142Z\",\n" +
//                "    \"learningRegime\": \"BAD_NORMAL_GOOD_STATUS_DEPENDING\",\n" +
//                "    \"password\": \"12345678\",\n" +
//                "    \"status\": \"ACTIVE\"\n" +
//                "  },\n" +
//                "  \"id\": 1,\n" +
//                "  \"person\": {\n" +
//                "    \"firstName\": \"string\",\n" +
//                "    \"id\": 1,\n" +
//                "    \"image\": \"string\",\n" +
//                "    \"imageBase64\": \"string\",\n" +
//                "    \"imageType\": \"LINK\",\n" +
//                "    \"lastName\": \"string\"\n" +
//                "  }\n" +
//                "}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void confirmRegistration() throws Exception {
        mockMvc.perform(post("/api/registration-confirm")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("132aksdj12e3"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(accountVerificationByEmailService, times(1)).accountVerification("132aksdj12e3");
    }

    @Test
    public void verificationToken() throws Exception {
        mockMvc.perform(put("/api/verification/token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("31627akjd72hnj27hf213njj12J"))
                .andExpect(status().isOk());
        verify(accountVerificationByEmailService, times(1)).getAccountEmail("31627akjd72hnj27hf213njj12J");
    }

    @Test
    public void sendConfirmationMail() throws Exception {
        mockMvc.perform(get("/api/confirmation-mail")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"body\": {},\n" +
                        "  \"statusCode\": \"100\",\n" +
                        "  \"statusCodeValue\": 0\n" +
                        "}"))
                .andExpect(status().isOk());
        verify(userService, times(1)).activateAccount();
    }

    @Test
    public void sendResetPasswordMail() throws Exception {
        mockMvc.perform(put("/api/reset/password")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@gmail.com\"}"))
                .andExpect(status().isOk());
        verify(accountService, times(1)).checkAccountStatusAndSendMail("{\"email\":\"admin@gmail.com\"}");
    }

    @Test
    public void createNewPasswordForUser() throws Exception {
        mockMvc.perform(put("/api/create/password")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"admin@gmail.com\",\n" +
                        "  \"password\": \"Password123\"\n" +
                        "}"))
                .andExpect(status().isOk());
        verify(accountService, times(1)).createNewAccountPassword("admin@gmail.com", "Password123");
    }

}
