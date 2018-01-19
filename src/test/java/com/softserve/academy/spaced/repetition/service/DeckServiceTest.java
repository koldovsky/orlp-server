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
import org.springframework.data.domain.Sort;
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

    private PageRequest pageRequest;
    private PageRequest adminPageRequest;

    private final int PAGE_NUMBER = 1;
    private final int QUANTITY_DECKS_IN_PAGE = 12;
    private final int QUANTITY_ADMIN_DECKS_IN_PAGE = 20;
    private final boolean PAGE_ASCENDING_ORDER = true;
    private final String PAGE_SORT_BY = "field";

    private PageRequest createPageRequest(int pageNumber, int quantityDecksInPage, String sortBy, boolean ascending) {
        return new PageRequest(pageNumber - 1, quantityDecksInPage,
                ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
    }

    @Before
    public void setUp() {
        user = DomainFactory.createUser(USER_ID, null, null, null, null);
        notOwnerUser = DomainFactory.createUser(NOT_OWNER_USER_ID, null, null, null, null);

        category = DomainFactory.createCategory(CATEGORY_ID, null, null, null, null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, category, 0D, user, null, null, null, null, null);
        course = DomainFactory.createCourse(COURSE_ID, null, null, null, 0D, false, user, category, null, null, null);

        pageRequest = createPageRequest(PAGE_NUMBER, QUANTITY_DECKS_IN_PAGE, PAGE_SORT_BY,
                PAGE_ASCENDING_ORDER);
        adminPageRequest = createPageRequest(PAGE_NUMBER, QUANTITY_ADMIN_DECKS_IN_PAGE, PAGE_SORT_BY,
                PAGE_ASCENDING_ORDER);
    }

    @Test
    public void testGetAllDecksByCourseId() {
        when(courseRepository.findOne(COURSE_ID)).thenReturn(course);

        List<Deck> result = deckService.getAllDecksByCourseId(COURSE_ID);
        verify(courseRepository).findOne(COURSE_ID);
        assertEquals(null, result);
    }

    @Test
    public void testGetAllDecksByCategoryId() {
        when(categoryRepository.findOne(CATEGORY_ID)).thenReturn(category);

        List<Deck> result = deckService.getAllDecksByCategoryId(CATEGORY_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);
        assertEquals(null, result);
    }

    @Test
    public void testGetAllOrderedDecks() {
        when(deckRepository.findAllByOrderByRatingDesc()).thenReturn(null);

        List<Deck> result = deckService.getAllOrderedDecks();
        verify(deckRepository).findAllByOrderByRatingDesc();
        assertEquals(null, result);
    }

    @Test
    public void testGetDeckById() {
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);

        Deck result = deckService.getDeckById(DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(deck, result);
    }

    @Test
    public void testGetAllCardsByDeckId() {
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);

        List<Card> result = deckService.getAllCardsByDeckId(DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        assertEquals(null, result);
    }

    @Test
    public void testAddDeckToCategory() {
        category.setDecks(new ArrayList<>());

        when(categoryRepository.findOne(CATEGORY_ID)).thenReturn(category);
        when(deckRepository.save(deck)).thenReturn(deck);

        deckService.addDeckToCategory(deck, CATEGORY_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).save(deck);

        category.setDecks(null);
    }

    @Test
    public void testAddDeckToCourse() {
        course.setDecks(new ArrayList<>());

        when(courseRepository.findOne(COURSE_ID)).thenReturn(course);
        when(deckRepository.save(deck)).thenReturn(deck);

        deckService.addDeckToCourse(deck, CATEGORY_ID, COURSE_ID);
        verify(courseRepository).findOne(COURSE_ID);
        verify(deckRepository).save(deck);

        course.setDecks(null);
    }

    @Test
    public void testUpdateDeck() {
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(category);
        when(deckRepository.save(deck)).thenReturn(deck);

        deckService.updateDeck(deck, DECK_ID, CATEGORY_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(deckRepository).save(deck);
    }

    @Test
    public void testUpdateDeckAdmin() {
        deck.setId(null);

        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(categoryRepository.findById(deck.getCategory().getId())).thenReturn(category);
        when(deckRepository.save(deck)).thenReturn(deck);

        Deck result = deckService.updateDeckAdmin(deck, DECK_ID);
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(deckRepository).save(deck);
        assertEquals(deck, result);

        deck.setId(DECK_ID);
    }

    @Test
    public void testDeleteDeckById() {
        doNothing().when(deckRepository).deleteDeckById(DECK_ID);

        deckService.deleteDeckById(DECK_ID);
        verify(deckRepository).deleteDeckById(DECK_ID);
    }

    @Test
    public void testCreateNewDeck() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(categoryRepository.findOne(CATEGORY_ID)).thenReturn(category);
        when(deckRepository.save(deck)).thenReturn(deck);

        deckService.createNewDeck(deck, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).save(deck);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCreateNewDeckByUnauthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        deckService.createNewDeck(deck, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testCreateNewDeckAdmin() throws NotAuthorisedUserException {
        deck.setId(null);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(categoryRepository.findById(deck.getCategory().getId())).thenReturn(category);
        when(deckRepository.save(deck)).thenReturn(deck);

        Deck result = deckService.createNewDeckAdmin(deck);
        verify(userService).getAuthorizedUser();
        verify(categoryRepository).findById(deck.getCategory().getId());
        verify(deckRepository).save(deck);
        assertEquals(deck, result);

        deck.setId(DECK_ID);
    }

    @Test
    public void testDeleteOwnDeck() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);

        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteOwnDeckThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testDeleteOwnDeckByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);

        deckService.deleteOwnDeck(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testUpdateOwnDeck() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(categoryRepository.findOne(CATEGORY_ID)).thenReturn(category);
        when(deckRepository.save(deck)).thenReturn(deck);

        deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).save(deck);
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateOwnDeckThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testUpdateOwnDeckByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);

        deckService.updateOwnDeck(deck, DECK_ID, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testGetAllDecksByUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findAllByDeckOwnerIdEquals(user.getId())).thenReturn(new ArrayList<>());

        deckService.getAllDecksByUser();
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findAllByDeckOwnerIdEquals(user.getId());
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetAllDeckByUnauthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        deckService.getAllDecksByUser();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetDeckUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);

        deckService.getDeckUser(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetDeckUserThatNotFound() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(null);

        deckService.getDeckUser(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test(expected = NotOwnerOperationException.class)
    public void testGetDeckUserByNotOwnerUser() throws NotAuthorisedUserException, NotOwnerOperationException {
        when(userService.getAuthorizedUser()).thenReturn(notOwnerUser);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);

        deckService.getDeckUser(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
    }

    @Test
    public void testGetPageWithDecksByCategory() {
        when(categoryRepository.findOne(CATEGORY_ID)).thenReturn(category);
        when(deckRepository.findAllByCategoryEquals(category, pageRequest)).thenReturn(any(Page.class));

        deckService.getPageWithDecksByCategory(CATEGORY_ID, PAGE_NUMBER, PAGE_SORT_BY, PAGE_ASCENDING_ORDER);
        verify(categoryRepository).findOne(CATEGORY_ID);
        verify(deckRepository).findAllByCategoryEquals(category, pageRequest);
    }

    @Test
    public void testGetPageWithAllAdminDecks() {
        when(deckRepository.findAll(adminPageRequest)).thenReturn(any(Page.class));

        deckService.getPageWithAllAdminDecks(PAGE_NUMBER, PAGE_SORT_BY, PAGE_ASCENDING_ORDER);
        verify(deckRepository).findAll(adminPageRequest);
    }

    @Test
    public void testGetSyntaxToHighlight() {
        when(deckRepository.getDeckById(DECK_ID)).thenReturn(deck);

        deckService.getSynthaxToHightlight(DECK_ID);
        verify(deckRepository).getDeckById(DECK_ID);
    }

}
