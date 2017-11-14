package com.softserve.academy.spaced.repetition.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DeckControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private DeckController deckController;

    @Mock
    private DeckService deckService;

    private final static int QUANTITY_ADMIN_DECKS_IN_PAGE = 20;
    private final static int QUANTITY_DECKS_IN_PAGE = 12;

    final int NUMBER_PAGE = 1;
    final boolean ADMIN_DECKS_ASCENDING = true;
    final String ADMIN_DECKS_SORT_BY = "id";

    final boolean DECKS_BY_CATEGORY_ASCENDING = false;
    final int CATEGORY_ID = 1;
    final String SORT_BY = "name";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deckController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void getDecksById() throws Exception {
        when(deckService.getDeck(eq(4L))).thenReturn(createDeck());
        mockMvc.perform(get("/api/admin/decks/{deckId}", 4L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\": \"Java interview #4\"," +
                        "  \"owner\": \"admin@gmail.com\"," +
                        "  \"description\": \"Part 4\"," +
                        "  \"deckId\": 4," +
                        "  \"rating\": 0," +
                        "  \"category\": \"Java\"," +
                        "  \"categoryId\": 1," +
                        "  \"links\": [{" +
                        "        \"rel\":\"self\"," +
                        "          \"href\":\"http://localhost/api/admin/decks/4\"" +
                        "      }]" +
                        "}"));
    }

    private Deck createDeck() {
        Deck deck = createDeck(4L, "Java interview #4", "Part 4",
                "admin@gmail.com", 0, 1L, "Java");
        return deck;
    }

    @Test
    public void getDecksByPage() throws Exception {
        when(deckService.getPageWithAllAdminDecks(NUMBER_PAGE, ADMIN_DECKS_SORT_BY, ADMIN_DECKS_ASCENDING)).thenReturn(createDecks());
        mockMvc.perform(get("/api/admin/decks?p=" + NUMBER_PAGE + "&sortBy=" + ADMIN_DECKS_SORT_BY + "&asc=" + ADMIN_DECKS_ASCENDING)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", is(QUANTITY_ADMIN_DECKS_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(ADMIN_DECKS_ASCENDING)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(!ADMIN_DECKS_ASCENDING)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(ADMIN_DECKS_SORT_BY)));
    }

    public static Deck createDeck(long idDeck, String nameDeck, String description,
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

    private Page<Deck> createDecks() throws ParseException {
        List<Deck> deckList = new ArrayList<>();

        Deck deck = createDeck(1L, "Java interview #1", "Part 1",
                "admin@gmail.com", 0, 1L, "Java");
        Deck deck2 = createDeck(2L, "Java interview #2", "Part 2",
                "admin@gmail.com", 0, 1L, "Java");
        Deck deck3 = createDeck(3L, "Java interview #3", "Part 3",
                "admin@gmail.com", 0, 1L, "Java");
        Deck deck4 = createDeck(4L, "Java interview #4", "Part 4",
                "admin@gmail.com", 0, 1L, "Java");
        Deck deck5 = createDeck(5L, "C++ interview #1", "Part 1",
                "sgofforth1@tmall.com", 0, 2L, "C++");
        Deck deck6 = createDeck(6L, "C++ interview #2", "Part 2",
                "sgofforth1@tmall.com", 0, 2L, "C++");
        Deck deck7 = createDeck(7L, "C++ interview #3", "Part 3",
                "sgofforth1@tmall.com", 0, 2L, "C++");
        Deck deck8 = createDeck(8L, "C# interview", "Interview materials",
                "kewins2@gizmodo.com", 0, 3L, "C#");
        Deck deck9 = createDeck(9L, "PHP interview #1", "Part 1",
                "wlouys3@sitemeter.com", 0, 4L, "PHP");
        Deck deck10 = createDeck(10L, "PHP interview #2", "Part 2",
                "wlouys3@sitemeter.com", 0, 4L, "PHP");
        Deck deck11 = createDeck(11L, "JavaScript", "Interview materials",
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

        Page<Deck> deckPage = new PageImpl<>(deckList, new PageRequest(NUMBER_PAGE, QUANTITY_ADMIN_DECKS_IN_PAGE, Sort.Direction.ASC, ADMIN_DECKS_SORT_BY), deckList.size());
        return deckPage;
    }

    @Test
    public void getDecksByPageAndCategory() throws Exception {
        when(deckService.getPageWithDecksByCategory(CATEGORY_ID, NUMBER_PAGE, SORT_BY, DECKS_BY_CATEGORY_ASCENDING)).thenReturn(createDecksBySelectedCategory());
        mockMvc.perform(get("/api/category/" + CATEGORY_ID + "/decks?p=" + NUMBER_PAGE + "&sortBy=" + SORT_BY + "&asc=" + DECKS_BY_CATEGORY_ASCENDING)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", is(QUANTITY_DECKS_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(!DECKS_BY_CATEGORY_ASCENDING)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(DECKS_BY_CATEGORY_ASCENDING)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(SORT_BY)));
    }

    private Page<Deck> createDecksBySelectedCategory() throws ParseException {
        List<Deck> deckList = new ArrayList<>();

        int QUANTITY_ADMIN_DECKS = 4;
        for (int i = QUANTITY_ADMIN_DECKS; i >= 1; i--) {
            Deck deck = createDeck(i, "Java interview #" + i, "Part " + i, "admin@gmail.com", 0, 1L, "Java");
            deckList.add(deck);
        }
        Page<Deck> deckPage = new PageImpl<>(deckList, new PageRequest(NUMBER_PAGE, QUANTITY_DECKS_IN_PAGE, Sort.Direction.ASC, SORT_BY), deckList.size());
        return deckPage;
    }
}
