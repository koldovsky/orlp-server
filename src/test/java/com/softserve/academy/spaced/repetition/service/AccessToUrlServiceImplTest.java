package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.AuthorityName;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.impl.AccessToUrlServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class AccessToUrlServiceImplTest {

    private final Long CATEGORY_ID = 1L;
    private final Long COURSE_ID = 1L;
    private final Long CARD_ID = 1L;
    private final Long FOLDER_ID = 1L;
    private final Long DECK_ID = 1L;
    private final Long COMMENT_ID = 1L;
    private User user;
    private Deck deck;
    private Category category;
    private Course course;
    private Card card;
    private List<Course> courseList;
    private List<Deck> deckList;
    private List<Card> cardList;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserService userService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private FolderRepository folderRepository;
    @Mock
    private DeckRepository deckRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private DeckCommentRepository deckCommentRepository;
    @Mock
    private CourseCommentRepository courseCommentRepository;
    @InjectMocks
    private AccessToUrlServiceImpl accessToUrlService;

    @Before
    public void setUp() throws Exception {
        courseList = DomainFactory.createCourseList(course);
        deckList = DomainFactory.createDeckList(deck);
        cardList = DomainFactory.createCardList(card);
        user = DomainFactory.createUser(1L, new Account(), new Person(), new Folder(), null);
        course = DomainFactory.createCourse(COURSE_ID, null, null, null, 1L,
                true, null, category, deckList, null, null);
        category = DomainFactory.createCategory(CATEGORY_ID, null, null,
                null, null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, category
                , 1L,user, cardList, courseList, null, null, null);
        card = DomainFactory.createCard(CARD_ID, null, null, null, deck);

        Authority roleAdmin = new Authority();
        Authority roleUser = new Authority();
        roleAdmin.setName(AuthorityName.ROLE_ADMIN);
        roleUser.setName(AuthorityName.ROLE_USER);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(roleAdmin);
        authorities.add(roleUser);
        user.getAccount().setAuthorities(authorities);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(courseRepository.getAccessToCourse(CATEGORY_ID, COURSE_ID))
                .thenReturn(courseList);
        when(deckRepository.hasAccessToDeck(COURSE_ID, DECK_ID))
                .thenReturn(deckList);
        when(cardRepository.hasAccessToCard(DECK_ID, CARD_ID))
                .thenReturn(cardList);
    }

    @Test
    public void testHasAccessToCategory() {
        when(categoryRepository.hasAccessToCategory(CATEGORY_ID))
                .thenReturn(DomainFactory.createListOfCategory(CATEGORY_ID));

        boolean result = accessToUrlService.hasAccessToCategory(CATEGORY_ID);
        verify(categoryRepository).hasAccessToCategory(CATEGORY_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToCourse() {
        boolean result = accessToUrlService.hasAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToDeckFromFolder() {
        when(folderRepository.getAccessToDeckFromFolder(FOLDER_ID, DECK_ID))
                .thenReturn(deckList);

        boolean result = accessToUrlService.hasAccessToDeckFromFolder(FOLDER_ID, DECK_ID);
        verify(folderRepository).getAccessToDeckFromFolder(FOLDER_ID, DECK_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToCourseByCategory() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID))
                .thenReturn(courseList);

        boolean result = accessToUrlService.hasAccessToCourse(CATEGORY_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToDeck() {
        boolean result = accessToUrlService.hasAccessToDeck(CATEGORY_ID, COURSE_ID, DECK_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(deckRepository).hasAccessToDeck(COURSE_ID, DECK_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToDeckFromCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID))
                .thenReturn(deckList);

        boolean result = accessToUrlService.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToDeckByCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID))
                .thenReturn(deckList);

        boolean result = accessToUrlService.hasAccessToDeck(CATEGORY_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToCard() {
        boolean result = accessToUrlService.hasAccessToCard(DECK_ID, CARD_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToCardByCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID))
                .thenReturn(deckList);

        boolean result = accessToUrlService.hasAccessToCard(CARD_ID, DECK_ID, CARD_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToCardByCard() {
        boolean result = accessToUrlService.hasAccessToCard(CATEGORY_ID, COURSE_ID, DECK_ID, CARD_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(deckRepository).hasAccessToDeck(COURSE_ID, DECK_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);
        assertEquals(true, result);
    }

    @Test
    public void testHasAccessToFolder() throws NotAuthorisedUserException {
        boolean result = accessToUrlService.hasAccessToFolder(FOLDER_ID);
        verify(userService).getAuthorizedUser();
        assertEquals(false, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testHasAccessToFolderByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        accessToUrlService.hasAccessToFolder(FOLDER_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void hasAccessToDeleteCommentForCourse() throws NotAuthorisedUserException {
        when(courseCommentRepository.findOne(COMMENT_ID)).thenReturn(new CourseComment());

        boolean result = accessToUrlService.hasAccessToDeleteCommentForCourse(COMMENT_ID);
        verify(userService,times(2)).getAuthorizedUser();
        verify(courseCommentRepository).findOne(COMMENT_ID);
        assertEquals(true,result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void hasAccessToDeleteCommentForCourseByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        accessToUrlService.hasAccessToDeleteCommentForCourse(COMMENT_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void hasAccessToDeleteCommentForDeck() throws NotAuthorisedUserException {
        when(deckCommentRepository.findOne(COMMENT_ID)).thenReturn(new DeckComment());

        boolean result = accessToUrlService.hasAccessToDeleteCommentForDeck(COMMENT_ID);
        verify(userService,times(2)).getAuthorizedUser();
        verify(deckCommentRepository).findOne(COMMENT_ID);
        assertEquals(true,result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void hasAccessToDeleteCommentForDeckByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        accessToUrlService.hasAccessToDeleteCommentForDeck(COMMENT_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void hasAccessToUpdateCommentForDeck() throws NotAuthorisedUserException {
        when(deckCommentRepository.findOne(COMMENT_ID)).thenReturn(new DeckComment());

        boolean result = accessToUrlService.hasAccessToUpdateCommentForDeck(COMMENT_ID);
        verify(userService).getAuthorizedUser();
        verify(deckCommentRepository).findOne(COMMENT_ID);
        assertEquals(false,result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void hasAccessToUpdateCommentForDeckByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        accessToUrlService.hasAccessToUpdateCommentForDeck(COMMENT_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testHasAccessToUpdateCommentForCourse() throws NotAuthorisedUserException {
        when(courseCommentRepository.findOne(COMMENT_ID)).thenReturn(new CourseComment());

        boolean result = accessToUrlService.hasAccessToUpdateCommentForCourse(COMMENT_ID);
        verify(courseCommentRepository).findOne(COMMENT_ID);
        assertEquals(false,result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testHasAccessToUpdateCommentForCourseByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        accessToUrlService.hasAccessToUpdateCommentForCourse(COMMENT_ID);
        verify(userService).getAuthorizedUser();

    }
}