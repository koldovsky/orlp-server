package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import com.softserve.academy.spaced.repetition.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

    private ImageServiceImpl mockedImageService;
    @Mock
    private MailService mockedMailService;

    final int NUMBER_PAGE = 1;
    final String SORT_BY = "id";
    final int QUANTITY_USER_IN_PAGE = 20;

    @Before
    public void setUp() throws Exception {
        userServiceUnderTest = PowerMockito.spy(new UserServiceImpl());
        userServiceUnderTest.setUserRepository(userRepository);
        userServiceUnderTest.setDeckRepository(deckRepository);
        userServiceUnderTest.setPasswordEncoder(mockedPasswordEncoder);
        mockedImageService = PowerMockito.spy(new ImageServiceImpl());
        userServiceUnderTest.setImageService(mockedImageService);
        userServiceUnderTest.setMailService(mockedMailService);
    }

    Person mockedPerson = new Person("firstName", "lastName");
    User mockedUser = new User(new Account("","email1@email.com"), mockedPerson, new Folder());

    @Test
    public void testEditPersonalData() throws Exception {
        Person newPerson = new Person("newFirstName", "newLastName");
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        User editedUser = userServiceUnderTest.editPersonalData(newPerson);
        assertEquals("Change personal data", mockedUser, editedUser);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testNotAuthorizedEditPersonalData() throws Exception {
        Person newPerson = new Person("newFirstName", "newLastName");
        doThrow(NotAuthorisedUserException.class).when(userServiceUnderTest).getAuthorizedUser();
        userServiceUnderTest.editPersonalData(newPerson);
    }

    String newPassword;
    User user;

    @Test
    public void testChangePassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO("11111111", "22222222");
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
        assertEquals("PasswordEncoder invoked", passwordDTO.getNewPassword(), newPassword);
        assertEquals("NotificationMail is sent", mockedUser, user);
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
        }).when(mockedImageService).checkImageExtension(any());
        String base64String = mockedImageService.encodeToBase64(image);
        PowerMockito.doReturn(mockedUser).when(userServiceUnderTest, "getAuthorizedUser");
        User userWithUploadImage = userServiceUnderTest.uploadImage(image);
        assertEquals("checkImageExtension invoked", image, newImage);
        assertEquals("Image is uploaded", base64String, userWithUploadImage.getPerson().getImageBase64());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUploadImageException() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "", "", "".getBytes());
        doThrow(IllegalArgumentException.class).when(mockedImageService).checkImageExtension(eq(image));
        userServiceUnderTest.uploadImage(image);
    }

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

    @Test
    public void testUsersInPage() {
        assertEquals(SORT_BY + ": ASC", userServiceUnderTest.getUsersByPage(NUMBER_PAGE, SORT_BY, true).getSort().toString());
        assertTrue((userServiceUnderTest.getUsersByPage(NUMBER_PAGE, SORT_BY, true).getTotalPages()) == 3);
        assertTrue((userServiceUnderTest.getUsersByPage(NUMBER_PAGE, SORT_BY, true).getNumberOfElements()) == QUANTITY_USER_IN_PAGE);
    }
}