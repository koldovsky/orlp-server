package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.DTO.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.validators.DataFieldValidator;
import com.softserve.academy.spaced.repetition.service.validators.PasswordFieldValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class UserServiceTest {

    private UserService userServiceUnderTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeckRepository deckRepository;
    @Mock
    private PasswordEncoder mockedPasswordEncoder;

    private ImageService mockedImageService;
    @Mock
    private MailService mockedMailService;
    @Mock
    private DataFieldValidator mockedDataFieldValidator;
    @Mock
    private PasswordFieldValidator mockedPasswordFieldValidator;

    @Before
    public void setUp() throws Exception {
        userServiceUnderTest = PowerMockito.spy(new UserService());
        userServiceUnderTest.setUserRepository(userRepository);
        userServiceUnderTest.setDeckRepository(deckRepository);
        userServiceUnderTest.setPasswordEncoder(mockedPasswordEncoder);
        mockedImageService = PowerMockito.spy(new ImageService());
        userServiceUnderTest.setImageService(mockedImageService);
        userServiceUnderTest.setMailService(mockedMailService);
        userServiceUnderTest.setDataFieldValidator(mockedDataFieldValidator);
        userServiceUnderTest.setPasswordFieldValidator(mockedPasswordFieldValidator);
    }

    Person mockedPerson = new Person("firstName", "lastName");
    User mockedUser = new User(new Account("email1@email.com"), mockedPerson, new Folder());

    Person person;

    @Test
    public void testEditPersonalData() throws Exception {
        Person newPerson = new Person("newFirstName", "newLastName");
        User newUser = new User(new Account("email1@email.com"), newPerson, new Folder());
        doAnswer((Answer<Void>) invocation -> {
            person = invocation.getArgumentAt(0, Person.class);
            return null;
        }).when(mockedDataFieldValidator).validate(any());
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        User editedUser = userServiceUnderTest.editPersonalData(newPerson);
        assertEquals("DataFieldValidator invoked", newPerson, person);
        assertEquals("Change personal data", mockedUser, editedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditPersonalDataException() throws Exception {
        Person newPerson = new Person("", "");
        User mockedUser = new User(new Account("email1@email.com"),
                new Person("firstName", "lastName"), new Folder());
        doThrow(IllegalArgumentException.class).when(mockedDataFieldValidator).validate(eq(newPerson));
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        userServiceUnderTest.editPersonalData(newPerson);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testNotAuthorizedEditPersonalData() throws Exception {
        Person newPerson = new Person("newFirstName", "newLastName");
        doThrow(NotAuthorisedUserException.class).when(userServiceUnderTest).getAuthorizedUser();
        userServiceUnderTest.editPersonalData(newPerson);
    }

    PasswordDTO password;
    String newPassword;
    User user;

    @Test
    public void testChangePassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO("11111111", "22222222");
        doAnswer((Answer<Void>) invocation -> {
            password = invocation.getArgumentAt(0, PasswordDTO.class);
            return null;
        }).when(mockedPasswordFieldValidator).validate(any());
        doAnswer((Answer<Void>) invocation -> {
            newPassword = invocation.getArgumentAt(0, String.class);
            return null;
        }).when(mockedPasswordEncoder).encode(any());
        doAnswer((Answer<Void>) invocation -> {
            user = invocation.getArgumentAt(0, User.class);
            return null;
        }).when(mockedMailService).sendPasswordNotificationMail(any());
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        userServiceUnderTest.changePassword(passwordDTO);
        assertEquals("PasswordFieldValidator invoked", passwordDTO, password);
        assertEquals("PasswordEncoder invoked", passwordDTO.getNewPassword(), newPassword);
        assertEquals("NotificationMail is sent", mockedUser, user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordException() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO("", "");
        doThrow(IllegalArgumentException.class).when(mockedPasswordFieldValidator).validate(eq(passwordDTO));
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        userServiceUnderTest.changePassword(passwordDTO);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testNotAuthorizedChangePassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO("11111111", "22222222");
        doThrow(NotAuthorisedUserException.class).when(userServiceUnderTest).getAuthorizedUser();
        userServiceUnderTest.changePassword(passwordDTO);
    }

    MockMultipartFile newImage;

    @Test
    public void testUploadImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image",
                "", "application/json", "{\"image\": \"F:\\photo.jpg\"}".getBytes());
        PasswordDTO passwordDTO = new PasswordDTO("11111111", "22222222");
        doAnswer((Answer<Void>) invocation -> {
            newImage = invocation.getArgumentAt(0, MockMultipartFile.class);
            return null;
        }).when(mockedImageService).checkImageExtention(any());
        String base64String = mockedImageService.encodeToBase64(image);
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        User userWithUploadImage = userServiceUnderTest.uploadImage(image);
        assertEquals("checkImageExtention invoked", image, newImage);
        assertEquals("Image is uploaded", base64String, userWithUploadImage.getPerson().getImageBase64());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadImageException() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "", "", "".getBytes());
        doThrow(IllegalArgumentException.class).when(mockedImageService).checkImageExtention(eq(image));
        userServiceUnderTest.uploadImage(image);
    }

    @Test
    public void testDeleteAccount() throws Exception {
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        userServiceUnderTest.deleteAccount();
        assertEquals("NotificationMail is sent", mockedUser, user);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testNotAuthorizedDeleteAccount() throws Exception {
        doThrow(NotAuthorisedUserException.class).when(userServiceUnderTest).getAuthorizedUser();
        userServiceUnderTest.deleteAccount();
    }
}