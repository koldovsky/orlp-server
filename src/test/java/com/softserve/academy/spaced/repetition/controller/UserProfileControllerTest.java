package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonImageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPasswordDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPersonalInfoDTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserProfileService;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
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

import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileControllerTest {

    private final String IMAGE_BASE64 = "imageBase64";
    private final String FIRST_NAME = "firstName";
    private final String LAST_NAME = "lastName";
    private final String CURRENT_PASSWORD = "currentPassword";
    private final String NEW_PASSWORD = "newPassword";
    private final String UPDATE_PERSONAL_INFO_CONTENT = String.format("{ \"firstName\": \"%s\", \"lastName\": \"%s\" }", FIRST_NAME, LAST_NAME);
    private final String CHANGE_PASSWORD_CONTENT = String.format("{ \"currentPassword\": \"%s\", \"newPassword\": \"%s\" }", CURRENT_PASSWORD, NEW_PASSWORD);
    private final String UPLOAD_IMAGE_CONTENT = String.format("{ \"imageBase64\": \"%s\" }", IMAGE_BASE64);
    private MockMvc mockMvc;
    @InjectMocks
    private UserProfileController userProfileController;
    @Mock
    private UserProfileService userProfileService;
    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;
    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException, UserStatusException {
        final Long USER_ID = 1L;
        final Long PERSON_ID = 1L;
        final Long ACCOUNT_ID = 1L;
        final String MESSAGE_SOURCE_MESSAGE = "message";

        final Person person = DomainFactory.createPerson(PERSON_ID, FIRST_NAME, LAST_NAME, null, null, IMAGE_BASE64);
        final Account account = DomainFactory.createAccount(ACCOUNT_ID, null, null, null, null, false, null, null, null,
                null, null);
        final User user = DomainFactory.createUser(USER_ID, account, person, null, null);
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController)
                .setControllerAdvice(exceptionHandlerController)
                .build();

        when(userProfileService.getProfileData()).thenReturn(user);
        when(userProfileService.updatePersonalInfo(any(JsonPersonalInfoDTO.class))).thenReturn(person);
        when(userProfileService.changePassword(any(JsonPasswordDTO.class))).thenReturn(account);
        when(userProfileService.uploadProfileImage(any(JsonImageDTO.class))).thenReturn(person);
        doNothing().when(userProfileService).deleteProfileImage();
        doNothing().when(userProfileService).deleteProfile();
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
    }

    @Test
    public void testGetProfileData() throws Exception {
        mockMvc.perform(get("/api/profile")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userProfileService).getProfileData();
    }

    @Test
    public void testGetProfileDataByNotAuthorisedUser() throws Exception {
        when(userProfileService.getProfileData()).thenThrow(new NotAuthorisedUserException());
        mockMvc.perform(get("/api/profile")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(userProfileService).getProfileData();
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testUpdatePersonalInfo() throws Exception {
        mockMvc.perform(put("/api/profile/personal-info")
                .content(UPDATE_PERSONAL_INFO_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userProfileService).updatePersonalInfo(any(JsonPersonalInfoDTO.class));
    }

    @Test
    public void testUpdatePersonalInfoByNotAuthorisedUser() throws Exception {
        when(userProfileService.updatePersonalInfo(any(JsonPersonalInfoDTO.class))).thenThrow(new NotAuthorisedUserException());
        mockMvc.perform(put("/api/profile/personal-info")
                .content(UPDATE_PERSONAL_INFO_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(userProfileService).updatePersonalInfo(any(JsonPersonalInfoDTO.class));
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testChangePassword() throws Exception {
        mockMvc.perform(put("/api/profile/password")
                .content(CHANGE_PASSWORD_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userProfileService).changePassword(any(JsonPasswordDTO.class));
    }

    @Test
    public void testChangePasswordByNotAuthorisedUser() throws Exception {
        when(userProfileService.changePassword(any(JsonPasswordDTO.class))).thenThrow(new NotAuthorisedUserException());
        mockMvc.perform(put("/api/profile/password")
                .content(CHANGE_PASSWORD_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(userProfileService).changePassword(any(JsonPasswordDTO.class));
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testChangePasswordIfCurrentPasswordIncorrect() throws Exception {
        when(userProfileService.changePassword(any(JsonPasswordDTO.class))).thenThrow(new IllegalArgumentException());
        mockMvc.perform(put("/api/profile/password")
                .content(CHANGE_PASSWORD_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userProfileService).changePassword(any(JsonPasswordDTO.class));
    }

    @Test
    public void testUploadProfileImage() throws Exception {
        mockMvc.perform(post("/api/profile/image")
                .content(UPLOAD_IMAGE_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userProfileService).uploadProfileImage(any(JsonImageDTO.class));
    }

    @Test
    public void testUploadProfileImageByNotAuthorisedUser() throws Exception {
        when(userProfileService.uploadProfileImage(any(JsonImageDTO.class))).thenThrow(new NotAuthorisedUserException());
        mockMvc.perform(post("/api/profile/image")
                .content(UPLOAD_IMAGE_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(userProfileService).uploadProfileImage(any(JsonImageDTO.class));
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testUploadProfileImageIfImageQuotaExceeded() throws Exception {
        when(userProfileService.uploadProfileImage(any(JsonImageDTO.class))).thenThrow(new ImageRepositorySizeQuotaExceededException());
        mockMvc.perform(post("/api/profile/image")
                .content(UPLOAD_IMAGE_CONTENT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verify(userProfileService).uploadProfileImage(any(JsonImageDTO.class));
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testDeleteProfileImage() throws Exception {
        mockMvc.perform(delete("/api/profile/image")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userProfileService).deleteProfileImage();
    }

    @Test
    public void testDeleteProfileImageByNotAuthorisedUser() throws Exception {
        doThrow(new NotAuthorisedUserException()).when(userProfileService).deleteProfileImage();
        mockMvc.perform(delete("/api/profile/image")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(userProfileService).deleteProfileImage();
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testDeleteProfile() throws Exception {
        mockMvc.perform(delete("/api/profile")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userProfileService).deleteProfile();
    }

    @Test
    public void testDeleteProfileByNotAuthorisedUser() throws Exception {
        doThrow(NotAuthorisedUserException.class).when(userProfileService).deleteProfile();
        mockMvc.perform(delete("/api/profile")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(userProfileService).deleteProfile();
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }
}
