package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonImageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPasswordDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.JsonPersonalInfoDTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import com.softserve.academy.spaced.repetition.service.impl.UserProfileServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserProfileServiceTest {

    private final String IMAGE_BASE64 = "imageBase64";
    private final String CURRENT_PASSWORD = "currentPassword";
    private final String NEW_PASSWORD = "newPassword";
    @InjectMocks
    private UserProfileServiceImpl userProfileService;
    @Mock
    private UserService userService;
    @Mock
    private MailService mailService;
    @Mock
    private ImageService imageService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private MessageSource messageSource;
    private User user;
    private Person person;
    private Account account;
    private JsonPasswordDTO passwordDTO;
    private JsonImageDTO imageDTO;

    @Before
    public void setUp() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        final Long USER_ID = 1L;
        final Long PERSON_ID = 1L;
        final Long ACCOUNT_ID = 1L;
        final String ACCOUNT_EMAIL = "account@test.com";
        final String MESSAGE_SOURCE_MESSAGE = "message";

        person = DomainFactory.createPerson(PERSON_ID, null, null, null, null, IMAGE_BASE64);
        account = DomainFactory.createAccount(ACCOUNT_ID, null, ACCOUNT_EMAIL, null, null, false, null, new HashSet<>(),
                null, null, null);
        user = DomainFactory.createUser(USER_ID, account, person, null, null);
        passwordDTO = new JsonPasswordDTO(CURRENT_PASSWORD, NEW_PASSWORD);
        imageDTO = new JsonImageDTO(IMAGE_BASE64);

        when(userService.getAuthorizedUser()).thenReturn(user);
        doNothing().when(imageService).checkImageExtension(multipartFile);
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
        doNothing().when(mailService).sendPasswordNotificationMail(user);
    }

    @Test
    public void testGetProfileData() throws NotAuthorisedUserException, UserStatusException {
        User result = userProfileService.getProfileData();
        verify(userService).getAuthorizedUser();
        assertEquals(user, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetProfileDataByNotAuthorisedUser() throws NotAuthorisedUserException, UserStatusException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userProfileService.getProfileData();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testUpdatePersonalInfo() throws NotAuthorisedUserException {
        final String FIRST_NAME = "firstName";
        final String LAST_NAME = "lastName";
        final JsonPersonalInfoDTO PERSONAL_INFO_DTO = new JsonPersonalInfoDTO(FIRST_NAME, LAST_NAME);

        Person result = userProfileService.updatePersonalInfo(PERSONAL_INFO_DTO);
        verify(userService).getAuthorizedUser();
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testUpdatePersonalInfoByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userProfileService.updatePersonalInfo(null);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testChangePassword() throws NotAuthorisedUserException {
        final boolean PASSWORD_MATCH = true;
        final String ENCODED_STRING = "encodedString";

        when(passwordEncoder.matches(eq(CURRENT_PASSWORD), any(String.class))).thenReturn(PASSWORD_MATCH);
        when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn(ENCODED_STRING);

        Account result = userProfileService.changePassword(passwordDTO);
        verify(userService).getAuthorizedUser();
        verify(passwordEncoder).matches(eq(CURRENT_PASSWORD), any(String.class));
        verify(passwordEncoder).encode(NEW_PASSWORD);
        verify(mailService).sendPasswordNotificationMail(user);
        assertEquals(account.getPassword(), result.getPassword());
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testChangePasswordByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userProfileService.changePassword(passwordDTO);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordIfCurrentPasswordIncorrect() throws NotAuthorisedUserException {
        final boolean PASSWORD_MATCH = false;

        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(PASSWORD_MATCH);

        userProfileService.changePassword(passwordDTO);
        verify(userService).getAuthorizedUser();
        verify(passwordEncoder).matches(any(String.class), any(String.class));
    }

    @Test
    public void testUploadProfileImage() throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException {
        final boolean IMAGE_CAN_BE_ADDED = true;

        when(imageService.isImageCanBeAddedToProfile(user, IMAGE_BASE64)).thenReturn(IMAGE_CAN_BE_ADDED);

        Person result = userProfileService.uploadProfileImage(imageDTO);
        verify(userService).getAuthorizedUser();
        verify(imageService).isImageCanBeAddedToProfile(user, IMAGE_BASE64);
        assertEquals(person.getImageBase64(), result.getImageBase64());
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testUploadProfileImageByNotAuthorisedUser() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userProfileService.uploadProfileImage(imageDTO);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = ImageRepositorySizeQuotaExceededException.class)
    public void testUploadProfileImageIfImageQuotaExceeded() throws ImageRepositorySizeQuotaExceededException, NotAuthorisedUserException {
        when(imageService.isImageCanBeAddedToProfile(user, IMAGE_BASE64)).thenThrow(new ImageRepositorySizeQuotaExceededException());

        userProfileService.uploadProfileImage(imageDTO);
        verify(userService).getAuthorizedUser();
        verify(imageService).isImageCanBeAddedToProfile(user, IMAGE_BASE64);
    }

    @Test
    public void testDeleteProfileImage() throws NotAuthorisedUserException {
        userProfileService.deleteProfileImage();
        verify(userService).getAuthorizedUser();
        assertNull(person.getImageBase64());
        assertEquals(ImageType.NONE, person.getImageType());
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteProfileImageByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userProfileService.deleteProfileImage();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteAccount() throws NotAuthorisedUserException {
        userProfileService.deleteProfile();
        verify(userService).getAuthorizedUser();
        assertTrue(account.isDeactivated());
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteAccountByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userProfileService.deleteProfile();
        verify(userService).getAuthorizedUser();
    }
}
