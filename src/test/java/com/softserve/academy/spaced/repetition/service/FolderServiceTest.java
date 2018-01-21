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
    public void setUp() {
        folder = DomainFactory.createFolder(FOLDER_ID, new HashSet<>());
        user = DomainFactory.createUser(USER_ID, null, null, folder, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0D, user, null, null, null, null, null);
    }

    @Test
    public void testAddDeckToFolderById() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.getDeckById(DECK_ID)).thenReturn(deck);
        when(folderRepository.save(folder)).thenReturn(folder);

        Deck result = folderService.addDeckToFolderById(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).getDeckById(DECK_ID);
        verify(folderRepository).save(folder);
        assertEquals(deck, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddDeckToFolderByIdByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        folderService.addDeckToFolderById(DECK_ID);
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
        when(userService.getAuthorizedUser()).thenReturn(user);
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
    public void testDeleteDeckById() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(folderRepository.save(folder)).thenReturn(folder);

        folderService.deleteDeckById(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(folderRepository).save(folder);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteDeckByIdByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        folderService.deleteDeckById(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteDeckFromAllUsersFolderById() {
        List<Folder> folders = new ArrayList<>();
        folders.add(folder);

        when(folderRepository.getAllFolderWhereIdDecksEquals(DECK_ID)).thenReturn(folders);
        when(folderRepository.save(folder)).thenReturn(folder);

        folderService.deleteDeckFromAllUsersFolderById(DECK_ID);
        verify(folderRepository).getAllFolderWhereIdDecksEquals(DECK_ID);
        verify(folderRepository).save(folder);
    }

}
