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
public class DeckControllerTets {
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
                        "      \"rating\": 4," +
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

    private Page<Deck> createDecks() throws ParseException {
        List<Deck> deckList = new ArrayList<>();

        Deck deck = new Deck();
        deck.setName("Java interview #1");
        User user = new User();
        Account account = new Account();
        account.setEmail("admin@gmail.com");
        user.setAccount(account);
        deck.setDeckOwner(user);
        deck.setDescription("Part 1");
        deck.setId(1L);
        deck.setRating(4);
        Category category = new Category();
        category.setName("Java");
        category.setId(1L);
        deck.setCategory(category);

        Deck deck2 = new Deck();
        deck2.setName("Java interview #2");
        deck2.setDeckOwner(user);
        deck2.setDescription("Part 2");
        deck2.setId(2L);
        deck2.setRating(0);
        deck2.setCategory(category);

        Deck deck3 = new Deck();
        deck3.setName("Java interview #3");
        deck3.setDeckOwner(user);
        deck3.setDescription("Part 3");
        deck3.setId(3L);
        deck3.setRating(0);
        deck3.setCategory(category);

        Deck deck4 = new Deck();
        deck4.setName("Java interview #4");
        deck4.setDeckOwner(user);
        deck4.setDescription("Part 4");
        deck4.setId(4L);
        deck4.setRating(0);
        deck4.setCategory(category);

        Deck deck5 = new Deck();
        deck5.setName("C++ interview #1");
        User user2 = new User();
        Account account2 = new Account();
        account2.setEmail("sgofforth1@tmall.com");
        user2.setAccount(account2);
        deck5.setDeckOwner(user2);
        deck5.setDescription("Part 1");
        deck5.setId(5L);
        deck5.setRating(0);
        Category category2 = new Category();
        category2.setName("C++");
        category2.setId(2L);
        deck5.setCategory(category2);

        Deck deck6 = new Deck();
        deck6.setName("C++ interview #2");
        deck6.setDeckOwner(user2);
        deck6.setDescription("Part 2");
        deck6.setId(6L);
        deck6.setRating(0);
        deck6.setCategory(category2);

        Deck deck7 = new Deck();
        deck7.setName("C++ interview #3");
        deck7.setDeckOwner(user2);
        deck7.setDescription("Part 3");
        deck7.setId(7L);
        deck7.setRating(0);
        deck7.setCategory(category2);

        Deck deck8 = new Deck();
        deck8.setName("C# interview");
        User user3 = new User();
        Account account3 = new Account();
        account3.setEmail("kewins2@gizmodo.com");
        user3.setAccount(account3);
        deck8.setDeckOwner(user3);
        deck8.setDescription("Interview materials");
        deck8.setId(8L);
        deck8.setRating(0);
        Category category3 = new Category();
        category3.setName("C#");
        category3.setId(3L);
        deck8.setCategory(category3);

        Deck deck9 = new Deck();
        deck9.setName("PHP interview #1");
        User user4 = new User();
        Account account4 = new Account();
        account4.setEmail("wlouys3@sitemeter.com");
        user4.setAccount(account4);
        deck9.setDeckOwner(user4);
        deck9.setDescription("Part 1");
        deck9.setId(9L);
        deck9.setRating(0);
        Category category4 = new Category();
        category4.setName("PHP");
        category4.setId(4L);
        deck9.setCategory(category4);

        Deck deck10 = new Deck();
        deck10.setName("PHP interview #2");
        deck10.setDeckOwner(user4);
        deck10.setDescription("Part 2");
        deck10.setId(10L);
        deck10.setRating(0);
        deck10.setCategory(category4);

        Deck deck11 = new Deck();
        deck11.setName("JavaScript");
        User user5 = new User();
        Account account5 = new Account();
        account5.setEmail("ndadson4@mapy.cz");
        user5.setAccount(account5);
        deck11.setDeckOwner(user5);
        deck11.setDescription("Interview materials");
        deck11.setId(11L);
        deck11.setRating(0);
        Category category5 = new Category();
        category5.setName("JavaScript");
        category5.setId(10L);
        deck11.setCategory(category5);

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
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false/learn\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #3\"," +
                        "      \"description\": \"Part 3\"," +
                        "      \"rating\": 0," +
                        "      \"deckId\": 3," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false/learn\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #2\"," +
                        "      \"description\": \"Part 2\"," +
                        "      \"rating\": 0," +
                        "      \"deckId\": 2," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false/learn\"}]" +
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview #1\"," +
                        "      \"description\": \"Part 1\"," +
                        "      \"rating\": 4," +
                        "      \"deckId\": 1," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false\"},{\"rel\":\"cards\",\"href\":\"http://localhost/api/category/1/decks?pageNumber=1&sortBy=name&ascending=false/learn\"}]" +
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

        Deck deck = new Deck();
        deck.setName("Java interview #1");
        User user = new User();
        Account account = new Account();
        account.setEmail("admin@gmail.com");
        user.setAccount(account);
        deck.setDeckOwner(user);
        deck.setDescription("Part 1");
        deck.setId(1L);
        deck.setRating(4);
        Category category = new Category();
        category.setName("Java");
        category.setId(1L);
        deck.setCategory(category);

        Deck deck2 = new Deck();
        deck2.setName("Java interview #2");
        deck2.setDeckOwner(user);
        deck2.setDescription("Part 2");
        deck2.setId(2L);
        deck2.setRating(0);
        deck2.setCategory(category);

        Deck deck3 = new Deck();
        deck3.setName("Java interview #3");
        deck3.setDeckOwner(user);
        deck3.setDescription("Part 3");
        deck3.setId(3L);
        deck3.setRating(0);
        deck3.setCategory(category);

        Deck deck4 = new Deck();
        deck4.setName("Java interview #4");
        deck4.setDeckOwner(user);
        deck4.setDescription("Part 4");
        deck4.setId(4L);
        deck4.setRating(0);
        deck4.setCategory(category);

        deckList.add(deck4);
        deckList.add(deck3);
        deckList.add(deck2);
        deckList.add(deck);

        Page<Deck> auditPage = new PageImpl<>(deckList);
        return auditPage;
    }
}
