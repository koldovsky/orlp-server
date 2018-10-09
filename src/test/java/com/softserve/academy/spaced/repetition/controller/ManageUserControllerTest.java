package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.hamcrest.Matchers;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ManageUserControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private ManageUserController manageUserController;

    @Mock
    private UserService userService;

    final int NUMBER_PAGE = 1;
    final String SORT_BY = "id";
    final int QUANTITY_USER_IN_PAGE = 20;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(manageUserController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void getUserById() throws Exception {
        when(userService.getUserById(eq(1L))).thenReturn(createUser());
        mockMvc.perform(get("/api/admin/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"accountStatus\": \"ACTIVE\"," +
                        "  \"email\": \"admin@gmail.com\"," +
                        "  \"firstName\": \"Admin\"," +
                        "  \"lastName\": \"Admin\"," +
                        "  \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/admin/users/1\"}]" +
                        "}"));
        verify(userService, times(1)).getUserById(1L);
    }

    public User createUser(int idUser, int idPerson, String firstName, String lastName, long accountId,
                           AccountStatus accountStatus, String email) {
        User user = new User();
        user.setId(idUser);
        Person person = new Person();
        person.setId(idPerson);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        user.setPerson(person);
        Account account = new Account();
        account.setId(accountId);
        account.setStatus(accountStatus);
        account.setEmail(email);
        user.setAccount(account);
        return user;
    }

    private User createUser() {
        User user = createUser(1, 1, "Admin", "Admin",
                1L, AccountStatus.ACTIVE, "admin@gmail.com");
        return user;
    }

    private User createUser(AccountStatus accountStatus) {
        Account account = new Account();
        account.setStatus(accountStatus);
        User user = createUser(1, 1, "Admin", "Admin",
                1L, account.getStatus(), "admin@gmail.com");
        return user;
    }

    private User createUserWithDeck() {
        User user = new User();
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Admin");
        person.setLastName("Admin");
        user.setPerson(person);

        Folder folder = new Folder();
        folder.setId(1L);

        Account account = new Account();
        account.setId(1L);
        account.setStatus(AccountStatus.ACTIVE);
        account.setEmail("admin@gmail.com");
        user.setAccount(account);

        Set<Deck> decks = new HashSet<>();
        Deck deck = new Deck();
        deck.setId(1L);
        deck.setName("Java");
        deck.setDescription("Practice in Java");
        decks.add(deck);
        folder.setDecks(decks);
        user.setFolder(folder);

        return user;
    }


    @Test
    public void addExistingDeckToUsersFolder() throws Exception {
        when(userService.addExistingDeckToUsersFolder(eq(1L), eq(1L))).thenReturn(createUserWithDeck());
        mockMvc.perform(post("/api/admin/users/{userId}/deck/{deckId}", 1L, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Admin")))
                .andExpect(jsonPath("$.accountStatus", is("ACTIVE")));
        verify(userService, times(1)).addExistingDeckToUsersFolder(1L, 1L);
    }

    @Test
    public void removeDeckFromUsersFolder() throws Exception {
        when(userService.removeDeckFromUsersFolder(eq(1L), eq(1L))).thenReturn(createUserWithDeck());
        mockMvc.perform(delete("/api/admin/users/{userId}/deck/{deckId}", 1L, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Admin")))
                .andExpect(jsonPath("$.accountStatus", is("ACTIVE")));
        verify(userService, times(1)).removeDeckFromUsersFolder(1L, 1L);
    }

    private List<Deck> createDecksForUser() {
        User user = new User();
        user.setId(1L);
        Account account = new Account();
        Person person = new Person();
        Category category = new Category();
        category.setName("Java");
        category.setId(3L);

        account.setId(1L);
        account.setStatus(AccountStatus.ACTIVE);
        account.setEmail("admin@gmail.com");
        user.setAccount(account);

        person.setId(1L);
        person.setFirstName("Admin");
        person.setLastName("Admin");
        user.setPerson(person);

        Deck deck1 = new Deck();
        deck1.setId(1L);
        deck1.setName("Java 8");
        deck1.setDescription("Practice in Java 8");
        deck1.setRating(5.0);
        deck1.setDeckOwner(user);
        deck1.setCategory(category);

        Deck deck2 = new Deck();
        deck2.setId(2L);
        deck2.setName("Java 9");
        deck2.setDescription("Practice in Java 9");
        deck2.setRating(4.0);
        deck2.setDeckOwner(user);
        deck2.setCategory(category);

        List<Deck> decks = new ArrayList<>();
        decks.add(deck1);
        decks.add(deck2);

        return decks;
    }

    @Test
    public void getAllDecksFromUsersFolder() throws Exception {
        when(userService.getAllDecksFromUsersFolder(eq(1L))).thenReturn(createDecksForUser());
        mockMvc.perform(get("/api/admin/users/{userId}/decks", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name", is("Java 8")))
                .andExpect(jsonPath("$.[1].rating", is(4.0)));
        verify(userService, times(1)).getAllDecksFromUsersFolder(1L);
    }

    @Test
    public void setUsersStatusBlocked() throws Exception {
        when(userService.setUsersStatusBlocked(eq(1L))).thenReturn(createUser(AccountStatus.BLOCKED));
        mockMvc.perform(put("/api/admin/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountStatus", Matchers.is("BLOCKED")));
        verify(userService, times(1)).setUsersStatusBlocked(1L);

    }

    @Test
    public void setUsersStatusDeleted() throws Exception {
        when(userService.setUsersStatusDeleted(eq(1L))).thenReturn(createUser(AccountStatus.DELETED));
        mockMvc.perform(delete("/api/admin/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountStatus", Matchers.is("DELETED")));
        verify(userService, times(1)).setUsersStatusDeleted(1L);

    }

    @Test
    public void setUsersStatusActive() throws Exception {
        when(userService.setUsersStatusActive(eq(1L))).thenReturn(createUser(AccountStatus.ACTIVE));
        mockMvc.perform(post("/api/admin/users/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountStatus", Matchers.is("ACTIVE")));
        verify(userService, times(1)).setUsersStatusActive(1L);

    }

    @Test
    public void getUsersByPage() throws Exception {
        when(userService.getUsersByPage(NUMBER_PAGE, SORT_BY, true)).thenReturn(createUsers());
        mockMvc.perform(get("/api/admin/users?p=" + NUMBER_PAGE + "&sortBy=" + SORT_BY + "&asc=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.size", is(QUANTITY_USER_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(true)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(false)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(SORT_BY)));
        verify(userService, times(1)).getUsersByPage(NUMBER_PAGE, SORT_BY, true);
    }

    private Page<User> createUsers() {
        final int quantityUsers = 40;
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= quantityUsers; i++) {
            User user = createUser(i, i, "Admin", "Admin",
                    i, AccountStatus.ACTIVE, "admin" + i + "@gmail.com");
            users.add(user);
        }
        Page<User> userPage = new PageImpl<>(users, new PageRequest(NUMBER_PAGE - 1, QUANTITY_USER_IN_PAGE, Sort.Direction.ASC, SORT_BY), quantityUsers);
        return userPage;
    }
}
