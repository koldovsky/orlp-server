package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import com.softserve.academy.spaced.repetition.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
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
