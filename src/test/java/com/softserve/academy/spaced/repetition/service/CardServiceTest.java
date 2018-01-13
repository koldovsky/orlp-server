package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.CardServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {
    MockMvc mockMvc;

    private static final long CARD_ID = 1L;
    private final String CARD_ANSWER = "Answer";
    private final String CARD_QUESTION = "Question";
    private final String CARD_TITLE = "Title";

    @InjectMocks
    private CardServiceImpl cardServiceUnderTest;

    @Mock
    private CardRepository cardRepository;

    @Mock
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

//    @Before
//    public void setUp() throws Exception {
//        cardServiceUnderTest = new CardServiceImpl(cardRepository, deckRepository, mockedAccountService, mockedUserService, userCardQueueService, null);
//    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(cardRepository.getOne(1L).getId());
        mockMvc = MockMvcBuilders.standaloneSetup().build();
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
