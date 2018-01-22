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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class FolderServiceTest {

    @InjectMocks
    private FolderServiceImpl folderService;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserService userService;

    private User user;
    private Folder folder;
    private Deck deck;

    private final long USER_ID = 1L;
    private final long FOLDER_ID = 1L;
    private final long DECK_ID = 1L;

    @Before
    public void setUp() throws NotAuthorisedUserException {
        folder = DomainFactory.createFolder(FOLDER_ID, new HashSet<>());
        user = DomainFactory.createUser(USER_ID, null, null, folder, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0D, user, null, null, null, null, null);

        when(userService.getAuthorizedUser()).thenReturn(user);
    }

    @Test
    public void testAddDeckToFolder() throws NotAuthorisedUserException {
        when(deckRepository.getDeckById(DECK_ID)).thenReturn(deck);

        Deck result = folderService.addDeckToFolder(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).getDeckById(DECK_ID);
        assertEquals(deck, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddDeckToFolderByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        folderService.addDeckToFolder(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetAllDecksByFolderId() {
        when(folderRepository.findOne(FOLDER_ID)).thenReturn(folder);

        List<Deck> result = folderService.getAllDecksByFolderId(FOLDER_ID);
        verify(folderRepository).findOne(FOLDER_ID);
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    public void testGetAllDecksIdFromFolder() throws NotAuthorisedUserException {
        when(folderRepository.selectAllDecksIdFromFolder(FOLDER_ID)).thenReturn(null);

        List<Long> result = folderService.getAllDecksIdFromFolder();
        verify(userService).getAuthorizedUser();
        verify(folderRepository).selectAllDecksIdFromFolder(FOLDER_ID);
        assertEquals(null, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetAllDecksIdFromFolderByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        folderService.getAllDecksIdFromFolder();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteDeckFromFolderById() throws NotAuthorisedUserException {
        folderService.deleteDeckFromFolderById(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteDeckFromFolderByIdByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        folderService.deleteDeckFromFolderById(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteDeckFromAllUsersFolderById() throws NotAuthorisedUserException {
        when(folderRepository.getAllFolderWhereIdDecksEquals(DECK_ID)).thenReturn(new ArrayList<>());

        folderService.deleteDeckFromAllUsersFoldersById(DECK_ID);
        verify(folderRepository).getAllFolderWhereIdDecksEquals(DECK_ID);
    }
}
