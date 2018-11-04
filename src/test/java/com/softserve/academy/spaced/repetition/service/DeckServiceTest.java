package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckCreateValidationDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckEditByAdminDTO;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.DeckServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class DeckServiceTest {

    private final long USER_ID = 1L;
    private final long CATEGORY_ID = 1L;
    private final long COURSE_ID = 1L;
    private final long DECK_ID = 1L;
    private final String DECK_SYNTAX_TO_HIGHLIGHT = "syntax_to_highlight";
    private final int PAGE_NUMBER = 1;
    private final boolean PAGE_ASCENDING_ORDER = true;
    private final String PAGE_SORT_BY = "sort_by";
    @InjectMocks
    private DeckServiceImpl deckService;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    @Mock
    private FolderService folderService;
    @Mock
    private MessageSource messageSource;
    private User notOwnerUser;
    private Category category;
    private Deck deck;
    private DeckCreateValidationDTO deckDTO;

    @Before
    public void setUp() throws NotAuthorisedUserException {
        final Long NOT_OWNER_USER_ID = 42L;
        final String MESSAGE_SOURCE_MESSAGE = "message";

        final User user = DomainFactory.createUser(USER_ID, null, null, null, null);
        notOwnerUser = DomainFactory.createUser(NOT_OWNER_USER_ID, null, null, null, null);
        category = DomainFactory.createCategory(CATEGORY_ID, null, null, null, null, new ArrayList<>());
        final Course course = DomainFactory.createCourse(COURSE_ID, null, null, null, 0D, false, user, category, new ArrayList<>(),
                null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, DECK_SYNTAX_TO_HIGHLIGHT, category, 0D, user, null, null,
                null, null);

        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(deckRepository.save(deck)).thenReturn(deck);
        doNothing().when(deckRepository).delete(deck);
        doNothing().when(deckRepository).delete(DECK_ID);
        when(courseRepository.findOne(COURSE_ID)).thenReturn(course);
        when(categoryRepository.findOne(CATEGORY_ID)).thenReturn(category);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(category);
    }

    @Test
    public void testGetAllDecksByCourseId() {
        List<Deck> result = deckService.getAllDecks(COURSE_ID);
        verify(courseRepository).findOne(COURSE_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetAllDecksByCategoryId() {
        List<Deck> result = deckService.getAllDecksByCategory(CATEGORY_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);
        assertNotNull(result);
    }

    @Test
    public void testGetAllOrderedDecks() {
        when(deckRepository.findAllByHiddenFalseOrderByRatingDesc()).thenReturn(null);

        List<Deck> result = deckService.getAllOrderedDecks();
        verify(deckRepository).findAllByHiddenFalseOrderByRatingDesc();
        assertNull(result);
    }

    @Test
    public void testGetDeckById() {
        Deck result = deckService.getDeck(DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(deck, result);
    }

    @Test
    public void testGetAllCardsByDeckId() {
        List<Card> result = deckService.getAllCardsByDeckId(DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        assertNull(result);
    }

    @Test
    public void testAddDeckToCourse() {
        deckService.addDeckToCourse(deck, COURSE_ID);
        verify(courseRepository).findOne(COURSE_ID);
        verify(deckRepository).save(deck);
    }

    @Test
    public void testUpdateDeckById() {
        deckService.updateDeck(deck, DECK_ID, CATEGORY_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(deckRepository).save(deck);
    }

    @Test
    public void testUpdateDeckAdminById() {
        DeckEditByAdminDTO deckEditDTO = new DeckEditByAdminDTO();
        deckEditDTO.setDescription("Description for edited card");
        deckEditDTO.setName("Edited card");
        deckEditDTO.setCategoryId(CATEGORY_ID);
        Deck result = deckService.updateDeckAdmin(deckEditDTO, DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(deckRepository).save(deck);
        assertEquals(deck, result);
    }

    @Test
    public void testDeleteDeckById() {
        deckService.deleteDeck(DECK_ID);
        verify(deckRepository).delete(DECK_ID);
    }

    @Test
    public void testCreateNewDeck() throws NotAuthorisedUserException {
        deckService.createNewDeck(deck, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).save(deck);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCreateNewDeckByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        deckService.createNewDeck(deck, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testCreateNewDeckByAdmin() throws NotAuthorisedUserException {
        when(deckRepository.save(any(Deck.class))).thenReturn(deck);

        deckDTO = new DeckCreateValidationDTO();
        deckDTO.setDescription("Description for new card");
        deckDTO.setName("New card");
        deckDTO.setCategoryId(CATEGORY_ID);
        folderService.addDeck(deck.getId());
        Deck result = deckService.createNewDeckAdmin(deckDTO);
        verify(userService).getAuthorizedUser();
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(deckRepository).save(deck);
        assertEquals(deck, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCreateNewDeckByAdminByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        deckService.createNewDeckAdmin(deckDTO);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteDeck() throws NotAuthorisedUserException, NotOwnerOperationException {
        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        verify(deckRepository).delete(deck);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteDeckThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testDeleteDeckByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);

        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testUpdateDeck() throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck result = deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).save(deck);
        assertEquals(deck, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateDeckThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testUpdateDeckByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);

        deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testGetAllUserDecks() throws NotAuthorisedUserException {
        when(deckRepository.findAllByDeckOwnerIdEquals(USER_ID)).thenReturn(null);

        List<Deck> result = deckService.getAllDecksByUser();
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findAllByDeckOwnerIdEquals(USER_ID);
        assertNull(result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetAllUserDecksByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        deckService.getAllDecksByUser();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetUserDeck() throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck result = deckService.getDeckUser(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(deck, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetUserDeckThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.getDeckUser(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testGetUserDeckByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);

        deckService.getDeckUser(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testGetPageWithDecksByCategory() {
        when(deckRepository.findAllByCategoryEqualsAndHiddenFalse(eq(category), any(PageRequest.class))).thenReturn(null);

        Page<Deck> result = deckService.getPageWithDecksByCategory(CATEGORY_ID, PAGE_NUMBER, PAGE_SORT_BY,
                PAGE_ASCENDING_ORDER);
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).findAllByCategoryEqualsAndHiddenFalse(eq(category), any(PageRequest.class));
        assertNull(result);
    }

    @Test
    public void testGetPageWithAllAdminDecks() {
        when(deckRepository.findAll(any(PageRequest.class))).thenReturn(null);

        Page<Deck> result = deckService.getPageWithAllAdminDecks(PAGE_NUMBER, PAGE_SORT_BY, PAGE_ASCENDING_ORDER);
        verify(deckRepository).findAll(any(PageRequest.class));
        assertEquals(null, result);
    }

    @Test
    public void testGetSyntaxToHighlight() {
        when(deckRepository.getDeckById(DECK_ID)).thenReturn(deck);

        String result = deckService.getSynthaxToHightlight(DECK_ID);
        verify(deckRepository).getDeckById(DECK_ID);
        assertEquals(DECK_SYNTAX_TO_HIGHLIGHT, result);
    }
}
