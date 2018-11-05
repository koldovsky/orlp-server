package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckCreateValidationDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckEditByAdminDTO;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class DeckControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private DeckController deckController;


    @Mock
    private DeckService deckService;

    @Mock
    private FolderService folderService;

    private final static int QUANTITY_ADMIN_DECKS_IN_PAGE = 20;
    private final static int QUANTITY_DECKS_IN_PAGE = 12;

    final private int NUMBER_PAGE = 1;
    final private String ADMIN_DECKS_SORT_BY = "id";

    final private int CATEGORY_ID = 1;
    final private Long DECK_ID = 1L;
    final private String SORT_BY = "name";


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deckController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
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
        mockMvc.perform(get("/api/categories/" + CATEGORY_ID + "/decks?p=" + NUMBER_PAGE + "&sortBy=" + SORT_BY + "&asc=" + false)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(2)))
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
    @Test
    public void testGetAllDecksOrderByRating()throws Exception{
        when(deckService.getAllOrderedDecks()).thenReturn(createArrayOfDecks());
        mockMvc.perform(get("/api/decks/ordered")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].name", is("1")))
                .andExpect(jsonPath("$.[1].name", is("2")))
                .andExpect(jsonPath("$.[2].name", is("3")))
                .andExpect(jsonPath("$.[2].rating", is(3.0)));
    }

    private List<Deck> createArrayOfDecks(){
        List<Deck> list = new ArrayList<>();
        Deck deck1 = new Deck();
        deck1.setName("1");
        deck1.setDescription("Deck1");
        deck1.setRating(1.0);
        Deck deck2 = new Deck();
        deck2.setName("2");
        deck2.setDescription("Deck2");
        deck2.setRating(2.0);
        Deck deck3 = new Deck();
        deck3.setName("3");
        deck3.setDescription("Deck3");
        deck3.setRating(3.0);
        list.add(deck1);
        list.add(deck2);
        list.add(deck3);
        return list;
    }

    @Test
    public void testGetAllDecksByCourseId() throws Exception{
        when(deckService.getAllDecks(1L)).thenReturn(createArrayOfDecks());
        mockMvc.perform(get("/api/category/{categoryId}/courses/{courseId}/decks", 1L, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].name", is("1")))
                .andExpect(jsonPath("$.[1].name", is("2")))
                .andExpect(jsonPath("$.[2].name", is("3")))
                .andExpect(jsonPath("$.[2].rating", is(3.0)));
    }

    @Test
    public void testUpdateDeckAccess() throws Exception {
        Deck deck =new Deck();
        deck.setId(DECK_ID);
        deck.setHidden(true);
        when(deckService.toggleDeckAccess(DECK_ID)).thenReturn(deck);
        mockMvc.perform(put("/api/cabinet/decks/{deckId}/toggle/access", DECK_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hidden", is(true)));
        verify(deckService,times(1)).toggleDeckAccess(DECK_ID);
    }

    @Test
    public void testGetDeckById() throws Exception{
        when(deckService.getDeck(1L)).thenReturn(createDeck(1L,"deck1","testdeck1", "deck@deck.com", 1.0, 1,"java"));
        mockMvc.perform(get("/api/decks/{deckId}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("deck1")))
                .andExpect(jsonPath("description", is("testdeck1")))
                .andExpect(jsonPath("rating", is(1.0)))
                .andExpect(jsonPath("deckId", is(1)));
    }

    @Test
    public void testGetCardsByCategoryAndDeck() throws Exception{
        when(deckService.getAllCardsByDeckId(1L)).thenReturn(createArrayOfCards());
        mockMvc.perform(get("/api/categories/decks/{deckId}/cards", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].answer", is("A1")))
                .andExpect(jsonPath("$.[1].question", is("Q2")))
                .andExpect(jsonPath("$.[2].title", is("Card3")));
    }

    private List<Card> createArrayOfCards(){
        List<Card> list = new ArrayList<>();
        Card card1 = new Card("Card1", "Q1", "A1");
        Card card2 = new Card("Card2", "Q2", "A2");
        Card card3 = new Card("Card3", "Q3", "A3");
        list.add(card1);
        list.add(card2);
        list.add(card3);
        return list;

    }

    @Test
    public void testGetCardsByCourseAndDeck() throws Exception{
        when(deckService.getAllCardsByDeckId(1L)).thenReturn(createArrayOfCards());
        mockMvc.perform(get("/api/category/{categoryId}/courses/{courseId}/decks/{deckId}/cards", 1L, 1L, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].answer", is("A1")))
                .andExpect(jsonPath("$.[1].question", is("Q2")))
                .andExpect(jsonPath("$.[2].title", is("Card3")));
    }

    @Test
    public void testAddDeckToCourse() throws Exception{
        doNothing().when(deckService).addDeckToCourse(createTestDeck(), 1L);
        mockMvc.perform(post("/api/courses/{courseId}/decks", 1L)
                .content(new ObjectMapper().writeValueAsString(createTestDeck()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("testDeck")))
                .andExpect(jsonPath("$.rating", is(1.0)));
    }

    @Test
    public void testAddDeckForAdmin() throws Exception{
        DeckCreateValidationDTO dcvDTO = new DeckCreateValidationDTO();
        dcvDTO.setCategoryId(1L);
        dcvDTO.setDescription("deckdescription");
        dcvDTO.setName("testDeck");
        Deck deck = createTestDeck();
        when(deckService.createNewDeckAdmin(refEq(dcvDTO))).thenReturn(deck);
        mockMvc.perform(post("/api/admin/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dcvDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.owner", is("test@test.com")))
                .andExpect(jsonPath("$.name", is("testDeck")))
                .andExpect(jsonPath("$.description", is("deckdescription")))
                .andExpect(jsonPath("$.rating", is(1.0)));
    }

    public Deck createTestDeck() {
        Deck deck = new Deck();
        deck.setName("testDeck");
        User user = new User();
        user.setId(1L);
        Account account = new Account();
        account.setEmail("test@test.com");
        user.setAccount(account);
        deck.setDeckOwner(user);
        deck.setDescription("deckdescription");
        deck.setId(1L);
        deck.setRating(1.0);
        Category category = new Category();
        category.setName("deckcategoryname");
        category.setId(1L);
        deck.setCategory(category);
        return deck;
    }

    @Test
    public void testUpdateDeckForAdmin() throws Exception{
        when(deckService.updateDeckAdmin(any(DeckEditByAdminDTO.class), eq(1L))).thenReturn(createTestDeck());
        mockMvc.perform(put("/api/admin/decks/{deckId}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createTestDeck())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner", is("test@test.com")))
                .andExpect(jsonPath("$.name", is("testDeck")))
                .andExpect(jsonPath("$.description", is("deckdescription")))
                .andExpect(jsonPath("$.rating", is(1.0)));
        verify(deckService, Mockito.times(1)).updateDeckAdmin(any(DeckEditByAdminDTO.class), eq(1L));
        verifyNoMoreInteractions(deckService);
    }

    @Test
    public void testDeleteDeckForAdmin() throws Exception{
        doNothing().when(folderService).deleteDeckFromAllUsers(1L);
        doNothing().when(deckService).deleteDeck(1L);
        mockMvc.perform(delete("/api/admin/decks/{deckId}", 1L))
                .andExpect(status().isOk());
        verify(folderService, times(1)).deleteDeckFromAllUsers(1L);
        verify(deckService, times(1)).deleteDeck(1L);
    }

    @Test
    public void testDeleteOwnDeckByUser() throws Exception{
        List<Deck> list = new ArrayList<>();
        list.add(createTestDeck());
        doNothing().when(deckService).deleteOwnDeck(1L);
        when(deckService.getAllDecksByUser()).thenReturn(list);
        mockMvc.perform(delete("/api/decks/{deckId}", 1L))
                .andExpect(status().isOk());
        verify(deckService, times(1)).deleteOwnDeck(1L);
    }

    @Test
    public void testAddDeckForUser() throws Exception{
        Deck deck = createTestDeck();
        doNothing().when(deckService).createNewDeck(deck, 1L);
        when(folderService.addDeck(deck.getId())).thenReturn(deck);
        mockMvc.perform(post("/api/categories/{categoryId}/decks", 1L)
                .content(new ObjectMapper().writeValueAsString(deck))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.owner", is("test@test.com")))
                .andExpect(jsonPath("$.rating", is(1.0)));
    }

    @Test
    public void testUpdateDeckForUser() throws Exception{
        when(deckService.updateOwnDeck(createTestDeck(), 1L, 1L)).thenReturn(createTestDeck());
        mockMvc.perform(put("/api/categories/{categoryId}/decks/{deckId}",1L, 1L)
                .content(new ObjectMapper().writeValueAsString(createTestDeck()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testDeck")))
                .andExpect(jsonPath("$.rating", is(1.0)));
    }

    @Test
    public void testGetAllDecksForUser() throws Exception {
        List<Deck> list = new ArrayList();
        list.add(createTestDeck());
        when(deckService.getAllDecksByUser()).thenReturn(list);
        mockMvc.perform(get("/api/users/folders/decks/own")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("testDeck")))
                .andExpect(jsonPath("$.[0].rating", is(1.0)));
    }

    @Test
    public void testGetOneDeckForUser() throws Exception{
        when(deckService.getDeckUser(1L)).thenReturn(createTestDeck());
        mockMvc.perform(get("/api/users/folders/decks/own/{deckId}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner", is("test@test.com")))
                .andExpect(jsonPath("$.rating", is(1.0)));
    }

}
