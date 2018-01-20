package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import com.softserve.academy.spaced.repetition.service.impl.AccessToUrlServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
    private final Long RATING = 1L;
    private User user;
    private Deck deck;
    private Category category;
    private Course course;
    private Folder folder;
    private Set<Folder> folderSet;
    private Card card;
    private Person person;
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
        person = DomainFactory.createPerson(1L,"","",null,"","");
        Account account = DomainFactory.createAccount(1L, "", "account@test.com", AuthenticationType.LOCAL,
                AccountStatus.ACTIVE, true, new Date(), null, LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING,
                10, null);
        user = DomainFactory.createUser(1L, account,person,folder,createCourseSet());
        folderSet = new HashSet<>();
        folderSet.add(folder);
        folder = DomainFactory.createFolder(1L, createDeckSet());
        course = DomainFactory.createCourse(COURSE_ID, null, null, null, RATING,
                true, null, category, createDeckList(), null, null);
        category = DomainFactory.createCategory(CATEGORY_ID, null, null,
                null, null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, category, RATING,
                user, createCardList(), createCourseList(), null, folderSet, null);
        card = DomainFactory.createCard(CARD_ID, null, null, null, deck);
    }

    private List<Course> createCourseList() {
        List<Course> courseList = new ArrayList<>();
        courseList.add(course);
        return courseList;
    }

    private Set<Course> createCourseSet() {
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        return courseSet;
    }

    private List<Deck> createDeckList() {
        List<Deck> deckList = new ArrayList<>();
        deckList.add(deck);
        return deckList;
    }

    private Set<Deck> createDeckSet() {
        Set<Deck> deckSet = new HashSet<>();
        deckSet.add(deck);
        return deckSet;
    }

    private List<Card> createCardList() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        return cardList;
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
                .thenReturn(createCourseList());

        accessToUrlService.hasAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
    }

    @Test
    public void testHasAccessToDeckFromFolder() {
        when(folderRepository.getAccessToDeckFromFolder(FOLDER_ID, DECK_ID))
                .thenReturn(createDeckList());

        accessToUrlService.hasAccessToDeckFromFolder(FOLDER_ID, DECK_ID);
        verify(folderRepository).getAccessToDeckFromFolder(FOLDER_ID, DECK_ID);
    }

    @Test
    public void testHasAccessToCourseByCategory() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID))
                .thenReturn(createCourseList());

        accessToUrlService.hasAccessToCourse(CATEGORY_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID);
    }

    @Test
    public void testHasAccessToDeck() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID, COURSE_ID))
                .thenReturn(createCourseList());
        when(deckRepository.hasAccessToDeck(COURSE_ID, DECK_ID))
                .thenReturn(createDeckList());

        accessToUrlService.hasAccessToDeck(CATEGORY_ID, COURSE_ID, DECK_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(deckRepository).hasAccessToDeck(COURSE_ID, DECK_ID);
    }

    @Test
    public void testHasAccessToDeckFromCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID))
                .thenReturn(createDeckList());

        accessToUrlService.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
    }

    @Test
    public void testHasAccessToDeckByCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID))
                .thenReturn(createDeckList());

        accessToUrlService.hasAccessToDeck(CATEGORY_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID);
    }

    @Test
    public void testHasAccessToCard() {
        when(cardRepository.hasAccessToCard(DECK_ID, CARD_ID))
                .thenReturn(createCardList());

        accessToUrlService.hasAccessToCard(DECK_ID, CARD_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);
    }

    @Test
    public void testHasAccessToCardByCategory() {
        when(deckRepository.hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID))
                .thenReturn(createDeckList());
        when(cardRepository.hasAccessToCard(DECK_ID, CARD_ID))
                .thenReturn(createCardList());

        accessToUrlService.hasAccessToCard(CARD_ID, DECK_ID, CARD_ID);
        verify(deckRepository).hasAccessToDeckFromCategory(CATEGORY_ID, DECK_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);

    }

    @Test
    public void testHasAccessToCardByCard() {
        when(courseRepository.getAccessToCourse(CATEGORY_ID, COURSE_ID))
                .thenReturn(createCourseList());
        when(deckRepository.hasAccessToDeck(COURSE_ID, DECK_ID))
                .thenReturn(createDeckList());
        when(cardRepository.hasAccessToCard(DECK_ID, CARD_ID))
                .thenReturn(createCardList());

        accessToUrlService.hasAccessToCard(CATEGORY_ID, COURSE_ID, DECK_ID, CARD_ID);
        verify(courseRepository).getAccessToCourse(CATEGORY_ID, COURSE_ID);
        verify(deckRepository).hasAccessToDeck(COURSE_ID, DECK_ID);
        verify(cardRepository).hasAccessToCard(DECK_ID, CARD_ID);
    }

    @Test
    public void testHasAccessToFolder() throws NotAuthorisedUserException {
//        when(userService.getAuthorizedUser().getFolder().getId()).thenReturn();
//
//        accessToUrlService.hasAccessToFolder(FOLDER_ID);
//        verify(userService).getAuthorizedUser();
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testHasAccessToFolderException() throws NotAuthorisedUserException {
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
        when(userService.getAuthorizedUser().getPerson()).thenReturn(person);
        when(courseCommentRepository.findOne(1L).getPerson()).thenReturn(person);

        accessToUrlService.hasAccessToUpdateCommentForCourse(1L);
        verify(userService).getAuthorizedUser();
        verify(courseCommentRepository).findOne(1L);
    }
}