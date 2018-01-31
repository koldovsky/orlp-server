package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.FolderRepository;
import com.softserve.academy.spaced.repetition.service.impl.FolderServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class FolderServiceTest {

    private final long FOLDER_ID = 1L;
    private final long DECK_ID = 1L;
    @InjectMocks
    private FolderServiceImpl folderService;
    @Mock
    private FolderRepository folderRepository;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private UserService userService;
    private Folder folder;
    private Deck deck;

    @Before
    public void setUp() throws NotAuthorisedUserException {
        final Long USER_ID = 1L;

        folder = DomainFactory.createFolder(FOLDER_ID, new HashSet<>());
        final User user = DomainFactory.createUser(USER_ID, null, null, folder, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0D, user, null, null, null, null);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.getDeckById(DECK_ID)).thenReturn(deck);
        when(folderRepository.save(folder)).thenReturn(folder);
        when(folderRepository.findOne(FOLDER_ID)).thenReturn(folder);
    }

    @Test
    public void testAddDeck() throws NotAuthorisedUserException {
        Deck result = folderService.addDeck(DECK_ID);
        verify(deckRepository).getDeckById(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(folderRepository).save(folder);
        assertEquals(deck, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddDeckToFolderByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        folderService.addDeck(DECK_ID);
        verify(deckRepository).getDeckById(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetAllDecksByFolderId() {
        List<Deck> result = folderService.getAllDecksByFolderId(FOLDER_ID);
        verify(folderRepository).findOne(FOLDER_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetAllDecksIdFromFolder() throws NotAuthorisedUserException {
        when(folderRepository.selectAllDeckIdWithFolder(FOLDER_ID)).thenReturn(null);

        List<Long> result = folderService.getAllDecksIdWithFolder();
        verify(userService).getAuthorizedUser();
        verify(folderRepository).selectAllDeckIdWithFolder(FOLDER_ID);
        assertNull(result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetAllDecksIdFromFolderByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        folderService.getAllDecksIdWithFolder();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteDeck() throws NotAuthorisedUserException {
        folderService.deleteDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(folderRepository).save(folder);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteDeckByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        folderService.deleteDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteDeckFromAllUsers() throws NotAuthorisedUserException {
        List<Folder> folders = new ArrayList<>();
        folders.add(folder);

        when(folderRepository.getAllFolderWhereIdDecksEquals(DECK_ID)).thenReturn(folders);

        folderService.deleteDeckFromAllUsers(DECK_ID);
        verify(folderRepository).getAllFolderWhereIdDecksEquals(DECK_ID);
        verify(folderRepository).save(folder);
    }
}
