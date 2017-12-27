package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    final String ADMIN_DECKS_SORT_BY = "id";

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
        when(deckService.getPageWithAllAdminDecks(NUMBER_PAGE, ADMIN_DECKS_SORT_BY, true)).thenReturn(createDecks());
        mockMvc.perform(get("/api/admin/decks?p=" + NUMBER_PAGE + "&sortBy=" + ADMIN_DECKS_SORT_BY + "&asc=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.size", is(QUANTITY_ADMIN_DECKS_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(true)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(false)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(ADMIN_DECKS_SORT_BY)));
    }

    public Deck createDeck(long idDeck, String nameDeck, String description,
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
        final int quantityDecks = 41;
        List<Deck> deckList = new ArrayList<>();
        for (int i = 1; i <= quantityDecks; i++) {
            Deck deck = createDeck(i, "Java interview #" + i, "Part " + i,
                    "admin@gmail.com", 0, i, "Java");
            deckList.add(deck);
        }

        Page<Deck> deckPage = new PageImpl<>(deckList, new PageRequest(NUMBER_PAGE - 1, QUANTITY_ADMIN_DECKS_IN_PAGE, Sort.Direction.ASC, ADMIN_DECKS_SORT_BY), quantityDecks);
        return deckPage;
    }

    @Test
    public void getDecksByPageAndCategory() throws Exception {
        when(deckService.getPageWithDecksByCategory(CATEGORY_ID, NUMBER_PAGE, SORT_BY, false)).thenReturn(createDecksBySelectedCategory());
        mockMvc.perform(get("/api/category/" + CATEGORY_ID + "/decks?p=" + NUMBER_PAGE + "&sortBy=" + SORT_BY + "&asc=" + false)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.size", is(QUANTITY_DECKS_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(true)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(false)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(SORT_BY)));
    }

    private Page<Deck> createDecksBySelectedCategory() throws ParseException {
        List<Deck> deckList = new ArrayList<>();

        int quantityAdminDecks = 14;
        for (int i = quantityAdminDecks; i >= 1; i--) {
            Deck deck = createDeck(i, "Java interview #" + i, "Part " + i, "admin@gmail.com", 0, 1L, "Java");
            deckList.add(deck);
        }
        Page<Deck> deckPage = new PageImpl<>(deckList, new PageRequest(NUMBER_PAGE - 1, QUANTITY_DECKS_IN_PAGE, Sort.Direction.ASC, SORT_BY), quantityAdminDecks);
        return deckPage;
    }
}
