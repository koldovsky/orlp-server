package com.softserve.academy.spaced.repetition.controller;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckOfUserManagedByAdminDTO;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
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
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RunWith(MockitoJUnitRunner.class)
public class DeckControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private DeckController deckController;

    @Mock
    private DeckService deckService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deckController)
                .setControllerAdvice(new ExceptionHandlerController())
                .alwaysDo(print())
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
        Deck deck = new Deck();
        deck.setId(4L);
        deck.setName("Java interview #4");
        deck.setDescription("Part 4");
        User user = new User();
        Account account = new Account();
        account.setEmail("admin@gmail.com");
        user.setAccount(account);
        deck.setDeckOwner(user);
        Category category = new Category();
        category.setId(1L);
        category.setName("Java");
        deck.setCategory(category);
        deck.setRating(0);
        return deck;
    }

    @Test
    public void getDecksByPage() throws Exception {
        int numberPage =1 ;
        String sortBy = "id";
        boolean ascending = true;
        when(deckService.getPageWithAllAdminDecks(numberPage,sortBy,ascending)).thenReturn(createDecks());
        mockMvc.perform(get("/api/admin/decks?p=1&sortBy=id&asc=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\": [{" +
                        "      \"name\": \"Java interview #1\"," +
                        "      \"owner\": \"admin@gmail.com\"," +
                        "      \"description\": \"Part 1\"," +
                        "      \"deckId\": 1," +
                        "      \"rating\": 0," +
                        "      \"category\": \"Java\"," +
                        "      \"categoryId\": 1," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/1\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #2\"," +
                        "      \"owner\": \"admin@gmail.com\"," +
                        "      \"description\": \"Part 2\"," +
                        "      \"deckId\": 2," +
                        "      \"rating\": 0," +
                        "      \"category\": \"Java\"," +
                        "      \"categoryId\": 1," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/2\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #3\"," +
                        "      \"owner\": \"admin@gmail.com\"," +
                        "      \"description\": \"Part 3\"," +
                        "      \"deckId\": 3," +
                        "      \"rating\": 0," +
                        "      \"category\": \"Java\"," +
                        "      \"categoryId\": 1," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/3\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #4\"," +
                        "      \"owner\": \"admin@gmail.com\"," +
                        "      \"description\": \"Part 4\"," +
                        "      \"deckId\": 4," +
                        "      \"rating\": 0," +
                        "      \"category\": \"Java\"," +
                        "      \"categoryId\": 1," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/4\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"C++ interview #1\"," +
                        "      \"owner\": \"sgofforth1@tmall.com\"," +
                        "      \"description\": \"Part 1\"," +
                        "      \"deckId\": 5," +
                        "      \"rating\": 0," +
                        "      \"category\": \"C++\"," +
                        "      \"categoryId\": 2," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/5\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"C++ interview #2\"," +
                        "      \"owner\": \"sgofforth1@tmall.com\"," +
                        "      \"description\": \"Part 2\"," +
                        "      \"deckId\": 6," +
                        "      \"rating\": 0," +
                        "      \"category\": \"C++\"," +
                        "      \"categoryId\": 2," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/6\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"C++ interview #3\"," +
                        "      \"owner\": \"sgofforth1@tmall.com\"," +
                        "      \"description\": \"Part 3\"," +
                        "      \"deckId\": 7," +
                        "      \"rating\": 0," +
                        "      \"category\": \"C++\"," +
                        "      \"categoryId\": 2," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/7\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"C# interview\"," +
                        "      \"owner\": \"kewins2@gizmodo.com\"," +
                        "      \"description\": \"Interview materials\"," +
                        "      \"deckId\": 8," +
                        "      \"rating\": 0," +
                        "      \"category\": \"C#\"," +
                        "      \"categoryId\": 3," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/8\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"PHP interview #1\"," +
                        "      \"owner\": \"wlouys3@sitemeter.com\"," +
                        "      \"description\": \"Part 1\"," +
                        "      \"deckId\": 9," +
                        "      \"rating\": 0," +
                        "      \"category\": \"PHP\"," +
                        "      \"categoryId\": 4," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/9\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"PHP interview #2\"," +
                        "      \"owner\": \"wlouys3@sitemeter.com\"," +
                        "      \"description\": \"Part 2\"," +
                        "      \"deckId\": 10," +
                        "      \"rating\": 0," +
                        "      \"category\": \"PHP\"," +
                        "      \"categoryId\": 4," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/10\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"JavaScript\"," +
                        "      \"owner\": \"ndadson4@mapy.cz\"," +
                        "      \"description\": \"Interview materials\"," +
                        "      \"deckId\": 11," +
                        "      \"rating\": 0," +
                        "      \"category\": \"JavaScript\"," +
                        "      \"categoryId\": 10," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/decks/11\"}]" +
                        "    }]," +
                        "  \"totalPages\": 1," +
                        "  \"totalElements\": 11," +
                        "  \"last\": true," +
                        "  \"size\": 0," +
                        "  \"number\": 0," +
                        "  \"numberOfElements\": 11," +
                        "  \"sort\": null," +
                        "  \"first\": true" +
                        "}"));
    }

    private Deck createDeck(long idDeck, String nameDeck, String descriptionDeck,
                            String accountEmail, double rating, long idCategory,
                            String nameCategory){
        Deck deck = new Deck();
        deck.setName(nameDeck);
        User user = new User();
        Account account = new Account();
        account.setEmail(accountEmail);
        user.setAccount(account);
        deck.setDeckOwner(user);
        deck.setDescription(descriptionDeck);
        deck.setId(idDeck);
        deck.setRating(rating);
        Category category = new Category();
        category.setName(nameCategory);
        category.setId(idCategory);
        deck.setCategory(category);
        return deck;
    }

    private Page<Deck> createDecks() throws ParseException {
        List<Deck> deckList = new ArrayList<>();

        Deck deck = createDeck(1L, "Java interview #1", "Part 1",
                "admin@gmail.com",0,1L,"Java");

        Deck deck2 = createDeck(2L, "Java interview #2", "Part 2",
                "admin@gmail.com",0,1L,"Java");

        Deck deck3 = createDeck(3L, "Java interview #3", "Part 3",
                "admin@gmail.com",0,1L,"Java");

        Deck deck4 = createDeck(4L, "Java interview #4", "Part 4",
                "admin@gmail.com",0,1L,"Java");

        Deck deck5 = createDeck(5L, "C++ interview #1", "Part 1",
                "sgofforth1@tmall.com",0,2L,"C++");

        Deck deck6 = createDeck(6L, "C++ interview #2", "Part 2",
                "sgofforth1@tmall.com",0,2L,"C++");

        Deck deck7 = createDeck(7L, "C++ interview #3", "Part 3",
                "sgofforth1@tmall.com",0,2L,"C++");

        Deck deck8 = createDeck(8L, "C# interview", "Interview materials",
                "kewins2@gizmodo.com",0,3L,"C#");

        Deck deck9 = createDeck(9L, "PHP interview #1", "Part 1",
                "wlouys3@sitemeter.com",0,4L,"PHP");

        Deck deck10 = createDeck(10L, "PHP interview #2", "Part 2",
                "wlouys3@sitemeter.com",0,4L,"PHP");

        Deck deck11 = createDeck(11L, "JavaScript", "Interview materials",
                "ndadson4@mapy.cz",0,10L,"JavaScript");

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

        Page<Deck> auditPage = new PageImpl<>(deckList);
        return auditPage;
    }

    @Test
    public void getDecksByPageAndCategory() throws Exception {
        int categoryId = 1;
        int numberPage =1 ;
        String sortBy = "name";
        boolean ascending = false;
        when(deckService.getPageWithDecksByCategory(categoryId, numberPage, sortBy, ascending)).thenReturn(createDecksBySelectedCategory());
        mockMvc.perform(get("/api/category/1/decks?p=1&sortBy=name&asc=false")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"content\": [" +
                        "    {" +
                        "      \"name\": \"Java interview #4\"," +
                        "      \"description\": \"Part 4\"," +
                        "      \"rating\": 0," +
                        "      \"deckId\": 4," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/decks/4/learn\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #3\"," +
                        "      \"description\": \"Part 3\"," +
                        "      \"rating\": 0," +
                        "      \"deckId\": 3," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/decks/3/learn\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #2\"," +
                        "      \"description\": \"Part 2\"," +
                        "      \"rating\": 0," +
                        "      \"deckId\": 2," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/decks/2/learn\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #1\"," +
                        "      \"description\": \"Part 1\"," +
                        "      \"rating\": 0," +
                        "      \"deckId\": 1," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/decks/1/learn\"}]" +
                        "    }" +
                        "  ]," +
                        "  \"totalPages\": 1," +
                        "  \"totalElements\": 4," +
                        "  \"last\": true," +
                        "  \"size\": 0," +
                        "  \"number\": 0," +
                        "  \"numberOfElements\": 4," +
                        "  \"first\": true," +
                        "  \"sort\": null"+
                        "}"));
    }

    private Page<Deck> createDecksBySelectedCategory() throws ParseException {
        List<Deck> deckList = new ArrayList<>();

        Deck deck = createDeck(1L, "Java interview #1",
                "Part 1", "admin@gmail.com",
                0, 1L,"Java");

        Deck deck2 = createDeck(2L, "Java interview #2",
                "Part 2", "admin@gmail.com",
                0, 1L,"Java");

        Deck deck3 = createDeck(3L, "Java interview #3",
                "Part 3", "admin@gmail.com",
                0, 1L,"Java");

        Deck deck4 = createDeck(4L, "Java interview #4",
                "Part 4", "admin@gmail.com",
                0, 1L,"Java");

        deckList.add(deck4);
        deckList.add(deck3);
        deckList.add(deck2);
        deckList.add(deck);

        Page<Deck> auditPage = new PageImpl<>(deckList);
        return auditPage;
    }
}
