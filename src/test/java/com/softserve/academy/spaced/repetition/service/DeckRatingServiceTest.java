package com.softserve.academy.spaced.repetition.service;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.domain.Folder;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRatingRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(ServiceTestConfig.class)
public class DeckRatingServiceTest {

    private DeckRatingService deckRatingServiceUderTest;

    @Autowired
    private DeckRatingRepository deckRatingRepository;

    @Mock
    private UserService mockedUserService;

    @Before
    public void setUp() throws Exception {
        deckRatingServiceUderTest = new DeckRatingService();
        deckRatingServiceUderTest.setUserService(mockedUserService);
        deckRatingServiceUderTest.setDeckRatingRepository(deckRatingRepository);

        User mockedUser = new User(new Account("email@email.com"),
                new Person("first", "last"),
                new Folder());
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
    }

    @Test
    public void addDeckRating() throws NotAuthorisedUserException {
        deckRatingServiceUderTest.addDeckRating(new DeckRating("email@email.com", null, 3), 3L);

        // TODO add assertion
    }

    @Test
    public void getDeckRatingById() {
        DeckRating deckRatingById = deckRatingServiceUderTest.getDeckRatingById(3L);

        // TODO add assertion
    }
}