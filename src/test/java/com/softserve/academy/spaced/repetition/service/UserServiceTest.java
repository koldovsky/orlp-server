package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.impl.UserServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.softserve.academy.spaced.repetition.domain.enums.AccountStatus.ACTIVE;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @Mock
    private MailService mailService;

    private User user;
    private Person person;
    private Account account;
    private Folder folder;
    private Deck deck;

    private final long USER_ID = 1L;
    private final long PERSON_ID = 1L;
    private final String PERSON_FISRT_NAME = "firstName";
    private final String PERSON_LAST_NAME = "lastName";
    private final long ACCOUNT_ID = 1L;
    private final long FOLDER_ID = 1L;
    private final long DECK_ID = 1L;
    private final String ACCOUNT_EMAIL = "account@example.com";
    private final AccountStatus ACCOUNT_STATUS = ACTIVE;

    private final Integer PAGE_REQUEST_NUMBER = 1;
    private final String PAGE_REQUEST_SORT_BY = "field";
    private final boolean PAGE_REQUEST_ASCENDING = true;

    @Before
    public void setUp() {
        person = DomainFactory.createPerson(PERSON_ID, PERSON_FISRT_NAME, PERSON_LAST_NAME, null, null, null);
        account = DomainFactory.createAccount(ACCOUNT_ID, null, ACCOUNT_EMAIL, null, ACCOUNT_STATUS, false, null, null,
                null, null, null);
        folder = DomainFactory.createFolder(FOLDER_ID, new HashSet<>());
        user = DomainFactory.createUser(USER_ID, account, person, folder, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0D, user, null, null, null, null);
    }

    @Test
    public void testAddUser() {
        when(userRepository.save(user)).thenReturn(user);

        userService.addUser(user);
        verify(userRepository).save(user);
    }

    @Test
    public void testFindUserByEmail() {
        when(userRepository.findUserByAccountEmail(ACCOUNT_EMAIL)).thenReturn(user);

        User result = userService.findUserByEmail(ACCOUNT_EMAIL);
        verify(userRepository).findUserByAccountEmail(ACCOUNT_EMAIL);
        assertEquals(user, result);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(null);

        List<User> result = userService.getAllUsers();
        verify(userRepository).findAll();
        assertNull(result);
    }

    @Test
    public void testSetUsersStatusActive() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        User result = userService.setUsersStatusActive(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertEquals(user, result);
    }

    @Test
    public void testSetUsersStatusDeleted() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        User result = userService.setUsersStatusDeleted(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertEquals(user, result);
    }

    @Test
    public void testSetUsersStatusBlocked() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        User result = userService.setUsersStatusBlocked(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertEquals(user, result);
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        User result = userService.getUserById(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertEquals(user, result);
    }

    @Test
    public void testAddExistingDeckToUsersFolder() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        ;

        User result = userService.addExistingDeckToUsersFolder(USER_ID, DECK_ID);
        verify(userRepository).findOne(USER_ID);
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(user, result);
    }

    @Test
    public void testGetAllCoursesByUserId() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        Set<Course> result = userService.getAllCoursesByUserId(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertNull(result);
    }

    @Test
    public void testRemoveDeckFromUsersFolder() {
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        userService.removeDeckFromUsersFolder(USER_ID, DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(userRepository).findOne(USER_ID);
    }

    @Test
    public void testGetAllDecksFromUsersFolder() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        List<Deck> result = userService.getAllDecksFromUsersFolder(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetUsersByPage() {
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(null);

        Page<User> result = userService.getUsersByPage(PAGE_REQUEST_NUMBER, PAGE_REQUEST_SORT_BY,
                PAGE_REQUEST_ASCENDING);
        verify(userRepository).findAll(any(PageRequest.class));
        assertNull(result);
    }
}