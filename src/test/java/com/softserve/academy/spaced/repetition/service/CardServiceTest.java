package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.CardServiceImpl;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class CardServiceTest {

    private static final long CARD_ID = 1L;
    private final String CARD_ANSWER = "Answer";
    private final String CARD_QUESTION = "Question";
    private final String CARD_TITLE = "Title";

    @Autowired
    private CardServiceImpl cardServiceUnderTest;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Mock
    private UserService mockedUserService;

    @Mock
    private AccountService mockedAccountService;

    @Mock
    private UserCardQueueService userCardQueueService;

    Card newCard = new Card(CARD_ID, CARD_QUESTION, CARD_ANSWER, CARD_TITLE);

    public Card getCardForTest(Long id) {
        Long newId = cardRepository.getOne(id).getId();
        String question = cardRepository.getOne(id).getQuestion();
        String answer = cardRepository.getOne(id).getAnswer();
        String title = cardRepository.getOne(id).getTitle();

        return new Card(newId, question, answer, title);
    }

    @Before
    public void setUp() throws Exception {
        cardServiceUnderTest = new CardServiceImpl(cardRepository, deckRepository, mockedAccountService, mockedUserService, userCardQueueService, null);
    }

    @Test
    public void getCardTest() {
        Card returnedCard = getCardForTest(1L);

        Card expectedResultCard = new Card(1L, "How many access modifiers do you know in Java?", "There are 4 access modifiers in Java: public, protected, " +
                "default and private", "Card 1");

        assertEquals("Result Card", returnedCard, expectedResultCard);
    }


    @Test
    public void testCardUpdate() {
        cardServiceUnderTest.updateCard(1L, newCard);
        assertEquals("Update Card", newCard, getCardForTest(1L));
    }


}
