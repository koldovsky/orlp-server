package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import com.softserve.academy.spaced.repetition.repository.AuthorityRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUser;
import com.softserve.academy.spaced.repetition.security.authentification.JwtUserFactory;
import com.softserve.academy.spaced.repetition.service.impl.UserServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserServiceTest {

    private final long USER_ID = 1L;
    private final String ACCOUNT_EMAIL = "account@test.com";
    private final String ACCOUNT_PASSWORD = "account_password";
    private final boolean ACCOUNT_DEACTIVATED = false;
    private final AccountStatus ACCOUNT_STATUS_ACTIVE = AccountStatus.ACTIVE;
    private final AccountStatus ACCOUNT_STATUS_BLOCKED = AccountStatus.BLOCKED;
    private final AuthorityName AUTHORITY_NAME_USER = AuthorityName.ROLE_USER;
    private final String PERSON_IMAGE_BASE64 = "image_base64";
    private final long DECK_ID = 1L;
    private final String AUTHENTICATION_NOT_AUTHORISED_USER = "anonymous_user";
    private final String PASSWORD_ENCODER_ENCODED_PASSWORD = "encoded_password";
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ImageService imageService;
    @Mock
    private MailService mailService;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private MultipartFile multipartFile;
    private User user;
    private Person person;
    private Account account;
    private Authority authority;
    private Deck deck;
    private Folder folder;

    @Before
    public void setUp() throws NotAuthorisedUserException, ImageRepositorySizeQuotaExceededException {
        final Long AUTHORITY_ID = 1L;
        final Long PERSON_ID = 1L;
        final String PERSON_FIRST_NAME = "firstName";
        final String PERSON_LAST_NAME = "lastName";
        final Long ACCOUNT_ID = 1L;
        final Long FOLDER_ID = 1L;

        person = DomainFactory.createPerson(PERSON_ID, PERSON_FIRST_NAME, PERSON_LAST_NAME, null, null, PERSON_IMAGE_BASE64);
        account = DomainFactory.createAccount(ACCOUNT_ID, ACCOUNT_PASSWORD, ACCOUNT_EMAIL, null, ACCOUNT_STATUS_ACTIVE,
                ACCOUNT_DEACTIVATED, null, new HashSet<>(), null, null, null);
        folder = DomainFactory.createFolder(FOLDER_ID, new HashSet<>());
        user = DomainFactory.createUser(USER_ID, account, person, folder, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0D, user, null, null, null, null);
        final JwtUser jwtUser = JwtUserFactory.create(account);
        authority = DomainFactory.createAuthority(AUTHORITY_ID, AUTHORITY_NAME_USER, null);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);
        when(userRepository.findUserByAccountEmail(ACCOUNT_EMAIL)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findOne(USER_ID)).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(deckRepository.getDeckByItsIdAndOwnerOfDeck(DECK_ID, USER_ID)).thenReturn(deck);
        doNothing().when(imageService).checkImageExtension(multipartFile);
    }

    @Test
    public void testAddUser() {
        userService.addUser(user);
        verify(userRepository).save(user);
    }

    @Test
    public void testFindUserByEmail() {
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
        User result = userService.setUsersStatusActive(USER_ID);
        verify(userRepository, times(2)).findOne(USER_ID);
        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    public void testSetUsersStatusDeleted() {
        User result = userService.setUsersStatusDeleted(USER_ID);
        verify(userRepository, times(2)).findOne(USER_ID);
        assertEquals(user, result);
    }

    @Test
    public void testSetUsersStatusBlocked() {
        User result = userService.setUsersStatusBlocked(USER_ID);
        verify(userRepository, times(2)).findOne(USER_ID);
        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    public void testGetUserById() {
        User result = userService.getUserById(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertEquals(user, result);
    }

    @Test
    public void testAddExistingDeckToUserFolder() {
        User result = userService.addExistingDeckToUsersFolder(USER_ID, DECK_ID);
        verify(userRepository).findOne(USER_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    public void testAddExistingDeckToUserFolderIfDeckAlreadyInFolder() {
        Set<Deck> decks = folder.getDecks();
        decks.add(deck);

        User result = userService.addExistingDeckToUsersFolder(USER_ID, DECK_ID);
        verify(userRepository).findOne(USER_ID);
        assertNull(result);

        decks.remove(deck);
    }

    @Test
    public void testGetNoAuthenticatedUserEmail() throws NotAuthorisedUserException {
        String result = userService.getNoAuthenticatedUserEmail();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        assertEquals(ACCOUNT_EMAIL, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetNoAuthenticatedUserEmailByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(authentication.getPrincipal()).thenReturn(AUTHENTICATION_NOT_AUTHORISED_USER);

        userService.getNoAuthenticatedUserEmail();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    @Test
    public void testGetAuthorisedUser() throws NotAuthorisedUserException {
        User result = userService.getAuthorizedUser();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userRepository).findUserByAccountEmail(ACCOUNT_EMAIL);
        assertEquals(user, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetAuthorisedUserByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(authentication.getPrincipal()).thenReturn(AUTHENTICATION_NOT_AUTHORISED_USER);

        userService.getAuthorizedUser();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    @Test
    public void testGetAllCoursesByUserId() {
        Set<Course> result = userService.getAllCoursesByUserId(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertNull(result);
    }

    @Test
    public void testDeleteDeckFromUsersFolder() {
        User result = userService.removeDeckFromUsersFolder(USER_ID, DECK_ID);
        verify(deckRepository).getDeckByItsIdAndOwnerOfDeck(DECK_ID, USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertNull(result);
    }

    @Test
    public void testDeleteDeckFromUsersFolderThatNotFoundByDeckIdAndUserId() {
        Set<Deck> decks = folder.getDecks();
        decks.add(deck);

        when(deckRepository.getDeckByItsIdAndOwnerOfDeck(DECK_ID, USER_ID)).thenReturn(null);

        User result = userService.removeDeckFromUsersFolder(USER_ID, DECK_ID);
        verify(deckRepository).getDeckByItsIdAndOwnerOfDeck(DECK_ID, USER_ID);
        verify(userRepository).findOne(USER_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    public void testGetAllDecksFromUserFolder() {
        List<Deck> result = userService.getAllDecksFromUsersFolder(USER_ID);
        verify(userRepository).findOne(USER_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetUsersByPage() {
        final Integer PAGE_REQUEST_NUMBER = 1;
        final String PAGE_REQUEST_SORT_BY = "column";
        final boolean PAGE_REQUEST_ASCENDING = true;

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(null);

        Page<User> result = userService.getUsersByPage(PAGE_REQUEST_NUMBER, PAGE_REQUEST_SORT_BY,
                PAGE_REQUEST_ASCENDING);
        verify(userRepository).findAll(any(PageRequest.class));
        assertNull(result);
    }

    @Test
    public void testActivateAccount() throws NotAuthorisedUserException {
        doNothing().when(mailService).sendActivationMail(ACCOUNT_EMAIL);

        userService.activateAccount();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(mailService).sendActivationMail(ACCOUNT_EMAIL);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testActivateAccountByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(authentication.getPrincipal()).thenReturn(AUTHENTICATION_NOT_AUTHORISED_USER);

        userService.activateAccount();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    @Test
    public void testDeleteAccount() throws NotAuthorisedUserException {
        final AccountStatus ACCOUNT_STATUS = AccountStatus.DELETED;

        userService.deleteAccount();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userRepository).save(user);
        assertEquals(user.getAccount().getStatus(), ACCOUNT_STATUS);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteAccountByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(authentication.getPrincipal()).thenReturn(AUTHENTICATION_NOT_AUTHORISED_USER);

        userService.deleteAccount();
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    @Test
    public void testInitializeNewUser() {
        final AuthenticationType AUTHENTICATION_TYPE = AuthenticationType.LOCAL;

        when(passwordEncoder.encode(ACCOUNT_PASSWORD)).thenReturn(PASSWORD_ENCODER_ENCODED_PASSWORD);
        when(authorityRepository.findAuthorityByName(AUTHORITY_NAME_USER)).thenReturn(authority);

        userService.initializeNewUser(account, ACCOUNT_EMAIL, ACCOUNT_STATUS_ACTIVE, ACCOUNT_DEACTIVATED,
                AUTHENTICATION_TYPE);
        verify(passwordEncoder).encode(ACCOUNT_PASSWORD);
        verify(authorityRepository).findAuthorityByName(AUTHORITY_NAME_USER);
    }

    @Test
    public void testIsUserStatusActive() throws UserStatusException {
        userService.isUserStatusActive(user);
    }

    @Test(expected = UserStatusException.class)
    public void testIsUserStatusActiveIfStatusNotActive() throws UserStatusException {
        account.setStatus(ACCOUNT_STATUS_BLOCKED);

        userService.isUserStatusActive(user);
    }
}
