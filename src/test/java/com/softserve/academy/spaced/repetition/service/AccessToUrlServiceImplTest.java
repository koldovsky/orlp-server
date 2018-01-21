package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
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
    private Deck deck;
    private Category category;
    private Course course;
    private Folder folder;
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
    public void setUp() {
        Set<Course> courseSet = DomainFactory.createCourseSet(course);
        courseList = DomainFactory.createCourseList(course);
        Set<Deck> deckSet = DomainFactory.createDeckSet(deck);
        deckList = DomainFactory.createDeckList(deck);
        cardList = DomainFactory.createCardList(card);
        Person person = DomainFactory.createPerson(1L, "", "", null, "", "");
        Account account = DomainFactory.createAccount(1L, "", "account@test.com", AuthenticationType.LOCAL,
                AccountStatus.ACTIVE, true, new Date(), null, LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING,
                10, null);
        Long USER_ID = 1L;
        User user = DomainFactory.createUser(USER_ID, account, person, folder, courseSet);
        Set<Folder> folderSet = new HashSet<>();
        folderSet.add(folder);
        folder = DomainFactory.createFolder(FOLDER_ID, deckSet);
        Long RATING = 1L;
        course = DomainFactory.createCourse(COURSE_ID, null, null, null, RATING,
                true, null, category, deckList, null, null);
        category = DomainFactory.createCategory(CATEGORY_ID, null, null,
                null, null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, category, RATING,
                user, cardList, courseList, null, folderSet, null);
        card = DomainFactory.createCard(CARD_ID, null, null, null, deck);
    }

    @Test
    public void testHasAccessToCategory() {
        when(categoryRepository.hasAccessToCategory(CATEGORY_ID))
                .thenReturn(DomainFactory.createListOfCategory(CATEGORY_ID));

        accessToUrlService.hasAccessToCategory(CATEGORY_ID);
        verify(categoryRepository).hasAccessToCategory(CATEGORY_ID);
    }

    @Test
    public void testHasAccessToCourse() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID, COURSE_ID))
                .thenReturn(courseList);

        accessToUrlService.hasAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
    }

    @Test
    public void testHasAccessToDeckFromFolder() {
        when(folderRepository.getAccessToDeckFromFolder(FOLDER_ID, DECK_ID))
                .thenReturn(deckList);

        accessToUrlService.hasAccessToDeckFromFolder(FOLDER_ID, DECK_ID);
        verify(folderRepository).getAccessToDeckFromFolder(FOLDER_ID, DECK_ID);
    }

    @Test
    public void testHasAccessToCourseByCategory() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID))
                .thenReturn(courseList);

        accessToUrlService.hasAccessToCourse(CATEGORY_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID);
    }

    @Test
    public void testHasAccessToDeck() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID, COURSE_ID))
                .thenReturn(courseList);
        when(deckRepository.hasAccessToDeck(COURSE_ID, DECK_ID))
                .thenReturn(deckList);

        accessToUrlService.hasAccessToDeck(CATEGORY_ID, COURSE_ID, DECK_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(deckRepository).hasAccessToDeck(COURSE_ID, DECK_ID);
    }

    @Test
    public void testHasAccessToDeckFromCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID))
                .thenReturn(deckList);

        accessToUrlService.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
    }

    @Test
    public void testHasAccessToDeckByCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID))
                .thenReturn(deckList);

        accessToUrlService.hasAccessToDeck(CATEGORY_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID);
    }

    @Test
    public void testHasAccessToCard() {
        when(cardRepository.hasAccessToCard(DECK_ID, CARD_ID))
                .thenReturn(cardList);

        accessToUrlService.hasAccessToCard(DECK_ID, CARD_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);
    }

    @Test
    public void testHasAccessToCardByCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID))
                .thenReturn(deckList);
        when(cardRepository.hasAccessToCard(DECK_ID, CARD_ID))
                .thenReturn(cardList);

        accessToUrlService.hasAccessToCard(CARD_ID, DECK_ID, CARD_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);

    }

    @Test
    public void testHasAccessToCardByCard() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID, COURSE_ID))
                .thenReturn(courseList);
        when(deckRepository.hasAccessToDeck(COURSE_ID, DECK_ID))
                .thenReturn(deckList);
        when(cardRepository.hasAccessToCard(DECK_ID, CARD_ID))
                .thenReturn(cardList);

        accessToUrlService.hasAccessToCard(CATEGORY_ID, COURSE_ID, DECK_ID, CARD_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(deckRepository).hasAccessToDeck(COURSE_ID, DECK_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);
    }

    @Test
    public void testHasAccessToFolder() throws NotAuthorisedUserException {

    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testHasAccessToFolderByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        accessToUrlService.hasAccessToFolder(FOLDER_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void hasAccessToDeleteCommentForCourse() {
    }

    @Test
    public void hasAccessToDeleteCommentForDeck() {
    }

    @Test
    public void hasAccessToUpdateCommentForDeck() {
    }

    @Test
    public void testHasAccessToUpdateCommentForCourse() throws NotAuthorisedUserException {

    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testHasAccessToUpdateCommentForCourseByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        accessToUrlService.hasAccessToUpdateCommentForCourse(COMMENT_ID);
        verify(userService).getAuthorizedUser();

    }
}