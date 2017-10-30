package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class DeckServiceTest {
    private DeckService deckService;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Mock
    private FolderService folderService;

    @Before
    public void setUp() throws Exception {
        deckService = new DeckService();
        deckService.setCourseRepository(courseRepository);
        deckService.setDeckRepository(deckRepository);
        deckService.setCardRepository(cardRepository);
        deckService.setCategoryRepository(categoryRepository);
        deckService.setCourseRepository(courseRepository);
        deckService.setUserService(userService);
        deckService.setFolderService(folderService);
    }

    public Deck createDeck(long idDeck,String nameDeck, String description,
                           String accountEmail, double rating, long idCategory,
                           String categoryName) {
        Deck deck = new Deck();
        deck.setName(nameDeck);
        User user = new User();
        Account account = new Account();
        account.setEmail(accountEmail);
        user.setAccount(account);
        deck.setDeckOwner(user);
        deck.setDescription(description);
        deck.setId(idDeck);
        deck.setRating(rating);
        Category category = new Category();
        category.setName(categoryName);
        category.setId(idCategory);
        deck.setCategory(category);
        return deck;
    }

    public Page<Deck> initializeDeckByPage() {
        List<Deck> deckList = new ArrayList<>();

        Deck deck = createDeck(1L,"Java interview #1", "Part 1",
                "admin@gmail.com", 0, 1L, "Java");

        Deck deck2 = createDeck(2L,"Java interview #2", "Part 2",
                "admin@gmail.com", 0, 1L, "Java");

        Deck deck3 = createDeck(3L,"Java interview #3", "Part 3",
                "admin@gmail.com", 0, 1L, "Java");

        Deck deck4 = createDeck(4L,"Java interview #4", "Part 4",
                "admin@gmail.com", 0, 1L, "Java");

        Deck deck5 = createDeck(5L,"C++ interview #1", "Part 1",
                "sgofforth1@tmall.com", 0, 2L, "C++");

        Deck deck6 = createDeck(6L,"C++ interview #2", "Part 2",
                "sgofforth1@tmall.com", 0, 2L, "C++");

        Deck deck7 = createDeck(7L,"C++ interview #3", "Part 3",
                "sgofforth1@tmall.com", 0, 2L, "C++");

        Deck deck8 = createDeck(8L,"C# interview", "Interview materials",
                "kewins2@gizmodo.com", 0, 3L, "C#");

        Deck deck9 = createDeck(9L,"PHP interview #1", "Part 1",
                "wlouys3@sitemeter.com", 0, 4L, "PHP");

        Deck deck10 = createDeck(10L,"PHP interview #2", "Part 2",
                "wlouys3@sitemeter.com", 0, 4L, "PHP");

        Deck deck11 = createDeck(11L,"JavaScript", "Interview materials",
                "ndadson4@mapy.cz", 0, 10L, "JavaScript");

        deckList.add(deck);
        deckList.add(deck2);
        deckList.add(deck3);
        deckList.add(deck4);
        deckList.add(deck5);
        deckList.add(deck6);
        deckList.add(deck7);
        deckList.add(deck8);
        deckList.add(deck9);
        deckList.add(deck10);
        deckList.add(deck11);

        Page<Deck> deckPage = new PageImpl<>(deckList);
        return deckPage;
    }

    @Test
    public void testDeckInPage() {
        Page<Deck> decks = initializeDeckByPage();

        final int pageNumber = 1;
        final String sortBy = "id";
        final boolean ascending = true;

        assertEquals(deckService.getPageWithAllAdminDecks(pageNumber, sortBy, ascending).getTotalElements(), decks.getTotalElements());
        assertEquals(deckService.getPageWithAllAdminDecks(pageNumber, sortBy, ascending).getTotalPages(), decks.getTotalPages());
        assertEquals(deckService.getPageWithAllAdminDecks(pageNumber, sortBy, ascending).getContent(), decks.getContent());
    }

    @Test
    public void testDeckInPageByCategory() {
        Page<Deck> decks = new PageImpl<>(initializeDeckByPage().getContent().subList(0,4));

        final int pageNumber = 1;
        final String sortBy = "id";
        final boolean ascending = true;
        final int categoryId = 1;
        List<Deck> deckList = decks.getContent().subList(0,4);
        deckList.forEach(System.out::print);
        assertEquals(deckService.getPageWithDecksByCategory(categoryId,pageNumber, sortBy, ascending).getTotalElements(), decks.getTotalElements());
        assertEquals(deckService.getPageWithDecksByCategory(categoryId,pageNumber, sortBy, ascending).getTotalPages(), decks.getTotalPages());
        assertEquals(deckService.getPageWithDecksByCategory(categoryId,pageNumber, sortBy, ascending).getContent(), decks.getContent().subList(0,4));
    }
}
