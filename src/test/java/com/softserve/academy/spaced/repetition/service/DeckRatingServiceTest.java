package com.softserve.academy.spaced.repetition.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.service.impl.DeckRatingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRatingRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class DeckRatingServiceTest {

    private static final long DECK_ID = 3L;

    private DeckRatingServiceImpl deckRatingServiceUnderTest;

    @Autowired
    private DeckRatingRepository deckRatingRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Mock
    private UserService mockedUserService;

    @Before
    public void setUp() throws Exception {
        deckRatingServiceUnderTest = new DeckRatingServiceImpl();
        deckRatingServiceUnderTest.setUserService(mockedUserService);
        deckRatingServiceUnderTest.setDeckRatingRepository(deckRatingRepository);
        deckRatingServiceUnderTest.setDeckRepository(deckRepository);
    }

    @Test
    public void testAverageDeckRating() throws NotAuthorisedUserException, UserStatusException {

        User mockedUser1 = new User(new Account("","email1@email.com"), new Person("first1", "last1"), new Folder());
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser1);
        deckRatingServiceUnderTest.addDeckRating(2, DECK_ID);

        User mockedUser2 = new User(new Account("","email2@email.com"), new Person("first2", "last2"), new Folder());
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser2);
        deckRatingServiceUnderTest.addDeckRating(4, DECK_ID);

        Deck deck = deckRepository.getDeckById(DECK_ID);

        assertEquals("Average rating.", 3.0, deck.getRating(), 0.0001);
    }

    @Test
    public void testGetRating() {
        DeckRating rating = deckRatingServiceUnderTest.getDeckRatingById(-77L);
        assertNull("Trying to find.", rating);
    }
}
