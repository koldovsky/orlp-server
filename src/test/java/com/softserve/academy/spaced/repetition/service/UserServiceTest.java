package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.impl.UserServiceImpl;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

import static com.softserve.academy.spaced.repetition.domain.enums.AccountStatus.ACTIVE;
import static org.mockito.Mockito.times;
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
    private Account account;
    private Folder folder;
    private Deck deck;

    private final long USER_ID = 1L;
    private final long ACCOUNT_ID = 1L;
    private final long FOLDER_ID = 1L;
    private final long DECK_ID = 1L;
    private final String ACCOUNT_EMAIL = "account@example.com";
    private final AccountStatus ACCOUNT_STATUS = ACTIVE;

    private User createUser(long userId, Account account, Folder folder) {
        User user = new User();
        user.setId(userId);
        user.setAccount(account);
        user.setCourses(new HashSet<>());
        user.setFolder(folder);
        return user;
    }

    private Account createAccount(long accountId, String email, AccountStatus accountStatus) {
        Account account = new Account();
        account.setId(accountId);
        account.setEmail(email);
        account.setStatus(accountStatus);
        return account;
    }

    private Folder createFolder(long folderId) {
        Folder folder = new Folder();
        folder.setId(folderId);
        folder.setDecks(new HashSet<>());
        return folder;
    }

    private Deck createDeck(long deckId, User deckOwner) {
        Deck deck = new Deck();
        deck.setId(deckId);
        deck.setDeckOwner(deckOwner);
        return deck;
    }


    @Before
    public void setUp() {
        account = createAccount(ACCOUNT_ID, ACCOUNT_EMAIL, ACCOUNT_STATUS);
        folder = createFolder(FOLDER_ID);
        user = createUser(USER_ID, account, folder);
        deck = createDeck(DECK_ID, user);
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

        userService.findUserByEmail(ACCOUNT_EMAIL);
        verify(userRepository).findUserByAccountEmail(ACCOUNT_EMAIL);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        userService.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    public void testSetUsersStatusActive() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.setUsersStatusActive(USER_ID);
        verify(userRepository, times(2)).findOne(USER_ID);
        verify(userRepository).save(user);
    }

    @Test
    public void testSetUsersStatusDeleted() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.setUsersStatusDeleted(USER_ID);
        verify(userRepository, times(2)).findOne(USER_ID);
        verify(userRepository).save(user);
    }

    @Test
    public void testSetUsersStatusBlocked() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.setUsersStatusBlocked(USER_ID);
        verify(userRepository, times(2)).findOne(USER_ID);
        verify(userRepository).save(user);
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        userService.getUserById(USER_ID);
        verify(userRepository).findOne(USER_ID);
    }

    @Test
    public void testAddExistingDeckToUserFolder() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(userRepository.save(user)).thenReturn(user);

        userService.addExistingDeckToUsersFolder(USER_ID, DECK_ID);
        verify(userRepository).findOne(USER_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(userRepository).save(user);
    }

    @Test
    public void testGetAllCoursesByUserId() {
        when(userRepository.findOne(USER_ID)).thenReturn(user);

        userService.getAllCoursesByUserId(USER_ID);
        verify(userRepository).findOne(USER_ID);
    }

    @Test
    public void testRemoveDeckFromUsersFolder() throws NotOwnerOperationException {
        Deck tempDeck = new Deck();
        tempDeck.setId(1L);
        folder.getDecks().add(tempDeck);

        when(deckRepository.getDeckByItsIdAndOwnerOfDeck(DECK_ID,USER_ID)).thenReturn(deck);
        when(userRepository.findOne(USER_ID)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.removeDeckFromUsersFolder(USER_ID, DECK_ID);
        verify(deckRepository).getDeckByItsIdAndOwnerOfDeck(DECK_ID, USER_ID);
        verify(userRepository).findOne(USER_ID);
        verify(userRepository).save(user);
    }

}