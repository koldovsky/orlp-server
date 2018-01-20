package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.CardRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.service.impl.CardRatingServiceImpl;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CardRatingServiceImplTest {

    @Mock
    private CardRatingRepository cardRatingRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CardRatingServiceImpl cardRatingService;

    private Long CATEGORY_ID = 1L;
    private Long COURSE_ID = 1L;
    private Long CARD_ID = 1L;
    private Long FOLDER_ID = 1L;
    private Long DECK_ID = 1L;
    private Long DECK_RATING = 1L;
    private int CARD_RATING = 1;
    private String EMAIL = "account@test.com";
    private User user;
    private Deck deck;
    private Category category;
    private Course course;
    private Folder folder;
    private Set<Folder> folderSet;
    private Card card;
    private Person person;
    private CardRating cardRating;

    @Before
    public void setUp() {
        person = DomainFactory.createPerson(1L,"","",null,"","");
        Account account = DomainFactory.createAccount(1L, "", EMAIL, AuthenticationType.LOCAL,
                AccountStatus.ACTIVE, true, new Date(), null, LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING,
                10, null);
                folderSet = new HashSet<>();
        folder = DomainFactory.createFolder(1L, createDeckSet());
        folderSet.add(folder);
        user = DomainFactory.createUser(1L, account,person,folder,createCourseSet());
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, category, DECK_RATING,
                user, createCardList(), createCourseList(), null, folderSet, null);
        cardRating = DomainFactory.createCardRating(1L,EMAIL,card,CARD_RATING);
        card = DomainFactory.createCard(CARD_ID, "Card 1", "How many access modifiers do you know in Java?",
                "There are 4 access modifiers in Java: public, protected, default and private", deck);
    }


    private Set<Deck> createDeckSet() {
        Set<Deck> deckSet = new HashSet<>();
        deckSet.add(deck);
        return deckSet;
    }

    private Set<Course> createCourseSet() {
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        return courseSet;
    }

    private List<Card> createCardList() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        return cardList;
    }

    private List<Course> createCourseList() {
        List<Course> courseList = new ArrayList<>();
        courseList.add(course);
        return courseList;
    }

    @Test()
    public void testAddCardRating() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(cardRatingRepository.findCardRatingByAccountEmailAndCard_Id(EMAIL, CARD_ID))
                .thenReturn(cardRating);
        when(cardRepository.findOne(CARD_ID)).thenReturn(card);
        when(cardRatingRepository.save(cardRating)).thenReturn(cardRating);
        when(cardRatingRepository.findRatingByCard_Id(CARD_ID)).thenReturn(3.0);
        when(cardRepository.save(card)).thenReturn(card);

        cardRatingService.addCardRating(cardRating,CARD_ID);
        verify(userService).getAuthorizedUser();
        verify(cardRatingRepository).findCardRatingByAccountEmailAndCard_Id(EMAIL,CARD_ID);
        verify(cardRatingRepository).findOne(CARD_ID);
        verify(cardRatingRepository).save(cardRating);
        verify(cardRatingRepository).findRatingByCard_Id(CARD_ID);
        verify(cardRepository).save(card);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddCardRatingByUnauthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        cardRatingService.addCardRating(cardRating,CARD_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetCardRatingById() {
        when(cardRatingRepository.findOne(CARD_ID)).thenReturn(cardRating);

        cardRatingService.getCardRatingById(CARD_ID);
        verify(cardRatingRepository).findOne(CARD_ID);
    }
}