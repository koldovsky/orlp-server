package com.softserve.academy.spaced.repetition.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class DeckServiceTest {

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

    private User user;
    private User notOwnerUser;
    private Category category;
    private Course course;
    private Deck deck;

    private final long USER_ID = 1L;
    private final long NOT_OWNER_USER_ID = 42L;
    private final long CATEGORY_ID = 1L;
    private final long COURSE_ID = 1L;
    private final long DECK_ID = 1L;
    private final String DECK_SYNTAX_TO_HIGHLIGHT = "syntax";

    private final int PAGE_NUMBER = 1;
    private final boolean PAGE_ASCENDING_ORDER = true;
    private final String PAGE_SORT_BY = "field";

    @Before
    public void setUp() throws NotAuthorisedUserException {
        user = DomainFactory.createUser(USER_ID, null, null, null, null);
        notOwnerUser = DomainFactory.createUser(NOT_OWNER_USER_ID, null, null, null, null);

        category = DomainFactory.createCategory(CATEGORY_ID, null, null, null, null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, DECK_SYNTAX_TO_HIGHLIGHT, category, 0D, user, null, null,
                null, null, null);
        course = DomainFactory.createCourse(COURSE_ID, null, null, null, 0D, false, user, category, null, null, null);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(deckRepository.save(deck)).thenReturn(deck);
        when(courseRepository.findOne(COURSE_ID)).thenReturn(course);
        when(categoryRepository.findOne(CATEGORY_ID)).thenReturn(category);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(category);
    }

    @Test
    public void testGetAllDecksByCourseId() {
        List<Deck> result = deckService.getAllDecksByCourseId(COURSE_ID);
        verify(courseRepository).findOne(COURSE_ID);
        assertEquals(null, result);
    }

    @Test
    public void testGetAllDecksByCategoryId() {
        List<Deck> result = deckService.getAllDecksByCategoryId(CATEGORY_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);
        assertEquals(null, result);
    }

    @Test
    public void testGetAllDecksOrderedDescByRating() {
        when(deckRepository.findAllByOrderByRatingDesc()).thenReturn(null);

        List<Deck> result = deckService.getAllDecksOrderedDescByRating();
        verify(deckRepository).findAllByOrderByRatingDesc();
        assertEquals(null, result);
    }

    @Test
    public void testGetDeckById() {
        Deck result = deckService.getDeckById(DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(deck, result);
    }

    @Test
    public void testGetAllCardsByDeckId() {
        List<Card> result = deckService.getAllCardsByDeckId(DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(null, result);
    }

    @Test
    public void testAddDeckToCategory() {
        category.setDecks(new ArrayList<>());

        deckService.addDeckToCategory(deck, CATEGORY_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);

        category.setDecks(null);
    }

    @Test
    public void testAddDeckToCourse() {
        course.setDecks(new ArrayList<>());

        deckService.addDeckToCourse(deck, CATEGORY_ID, COURSE_ID);
        verify(courseRepository).findOne(COURSE_ID);

        course.setDecks(null);
    }

    @Test
    public void testUpdateDeck() {
        deckService.updateDeck(deck, DECK_ID, CATEGORY_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
    }

    @Test
    public void testUpdateDeckAdmin() {
        Deck result = deckService.updateDeckAdmin(deck, DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        assertEquals(deck, result);
    }

    @Test
    public void testDeleteDeckById() {
        doNothing().when(deckRepository).deleteDeckById(DECK_ID);

        deckService.deleteDeckById(DECK_ID);
        verify(deckRepository).deleteDeckById(DECK_ID);
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
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        deckService.createNewDeck(deck, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testCreateNewDeckAdmin() throws NotAuthorisedUserException {
        deck.setId(null);

        Deck result = deckService.createNewDeckAdmin(deck);
        verify(userService).getAuthorizedUser();
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(deckRepository).save(deck);
        assertEquals(deck, result);

        deck.setId(DECK_ID);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCreateNewDeckAdminByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        deckService.createNewDeckAdmin(deck);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testDeleteOwnDeck() throws NotAuthorisedUserException, NotOwnerOperationException {
        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteOwnDeckThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testDeleteOwnDeckByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);

        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testUpdateOwnDeck() throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck result = deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);
        assertEquals(deck, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateOwnDeckThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testUpdateOwnDeckByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);

        deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testGetAllUserDecks() throws NotAuthorisedUserException {
        when(deckRepository.findAllByDeckOwnerIdEquals(USER_ID)).thenReturn(null);

        List<Deck> result = deckService.getAllUserDecks();
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findAllByDeckOwnerIdEquals(USER_ID);
        assertEquals(null, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetAllUserDecksByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        deckService.getAllUserDecks();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetUserDeckByDeckId() throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck result = deckService.getUserDeckByDeckId(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(deck, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetUserDeckByDeckIdThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.getUserDeckByDeckId(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testGetUserDeckByDeckIdByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);

        deckService.getUserDeckByDeckId(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testGetPageWithDecksByCategory() {
        when(deckRepository.findAllByCategoryEquals(eq(category), any(PageRequest.class))).thenReturn(null);

        Page<Deck> result = deckService.getPageWithDecksByCategory(CATEGORY_ID, PAGE_NUMBER, PAGE_SORT_BY,
                PAGE_ASCENDING_ORDER);
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).findAllByCategoryEquals(eq(category), any(PageRequest.class));
        assertEquals(null, result);
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

        String result = deckService.getSyntaxToHighlight(DECK_ID);
        verify(deckRepository).getDeckById(DECK_ID);
        assertEquals(DECK_SYNTAX_TO_HIGHLIGHT, result);
    }
}
