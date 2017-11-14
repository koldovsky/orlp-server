package com.softserve.academy.spaced.repetition.controller;

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
    final boolean ASCENDING = true;
    final int QUANTITY_USER_IN_PAGE = 20;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(manageUserController)
                .setControllerAdvice(new ExceptionHandlerController()).alwaysDo(print())
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

    public static User createUser(int idUser, int idPerson, String firstName, String lastName, long accountId,
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
        when(userService.getUsersByPage(NUMBER_PAGE, SORT_BY, ASCENDING)).thenReturn(createUsers());
        mockMvc.perform(get("/api/admin/users?p=" + NUMBER_PAGE + "&sortBy=" + SORT_BY + "&asc=" + ASCENDING)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.size", is(QUANTITY_USER_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(ASCENDING)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(!ASCENDING)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(SORT_BY)));
    }

    private Page<User> createUsers() {
        User user = createUser(1, 1, "Admin", "Admin",
                1L, AccountStatus.ACTIVE, "admin@gmail.com");
        User user2 = createUser(2, 2, "Lilith", "Penhall",
                2L, AccountStatus.ACTIVE, "sgofforth1@tmall.com");
        User user3 = createUser(3, 3, "Jammal", "Ritmeier",
                3L, AccountStatus.ACTIVE, "kewins2@gizmodo.com");
        User user4 = createUser(4, 4, "Lavena", "Beardow",
                4L, AccountStatus.ACTIVE, "wlouys3@sitemeter.com");
        User user5 = createUser(5, 5, "Shane", "Hulmes",
                5L, AccountStatus.ACTIVE, "ndadson4@mapy.cz");
        User user6 = createUser(6, 6, "Reinhold", "Gorthy",
                6L, AccountStatus.ACTIVE, "dmunnings5@home.pl");
        User user7 = createUser(7, 7, "Shannen", "Dutnell",
                7L, AccountStatus.ACTIVE, "dwalding6@ezinearticles.com");
        User user8 = createUser(8, 8, "Kane", "Mattiato",
                8L, AccountStatus.ACTIVE, "slorkin7@eepurl.com");
        User user9 = createUser(9, 9, "Garrard", "Urey",
                9L, AccountStatus.ACTIVE, "amaxstead8@cam.ac.uk");
        User user10 = createUser(10, 10, "Weidar", "Sturm",
                10L, AccountStatus.ACTIVE, "ftowne9@umich.edu");
        User user11 = createUser(11, 11, "Fitz", "Caldera",
                11L, AccountStatus.ACTIVE, "hkerforda@sogou.com");
        User user12 = createUser(12, 12, "Norrie", "Peek",
                12L, AccountStatus.ACTIVE, "ssindleb@baidu.com");
        User user13 = createUser(13, 13, "Clyde", "Stokey",
                13L, AccountStatus.ACTIVE, "aleedalc@i2i.jp");
        User user14 = createUser(14, 14, "Fey", "Harrison",
                14L, AccountStatus.ACTIVE, "ddowsd@accuweather.com");
        User user15 = createUser(15, 15, "Raul", "Adenot",
                15L, AccountStatus.ACTIVE, "cparkine@adobe.com");
        User user16 = createUser(16, 16, "Derrik", "Marc",
                16L, AccountStatus.ACTIVE, "lguittonf@state.tx.us");
        User user17 = createUser(17, 17, "Eugenius", "Jeffels",
                17L, AccountStatus.ACTIVE, "smelendezg@hp.com");
        User user18 = createUser(18, 18, "Corina", "Levin",
                18L, AccountStatus.ACTIVE, "dbraunthalh@gov.uk");
        User user19 = createUser(19, 19, "Joaquin", "Pepall",
                19L, AccountStatus.ACTIVE, "agooderridgei@amazon.de");
        User user20 = createUser(20, 20, "Benedetto", "Edwicke",
                20L, AccountStatus.ACTIVE, "dcauserj@e-recht24.de");

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        userList.add(user6);
        userList.add(user7);
        userList.add(user8);
        userList.add(user9);
        userList.add(user10);
        userList.add(user11);
        userList.add(user12);
        userList.add(user13);
        userList.add(user14);
        userList.add(user15);
        userList.add(user16);
        userList.add(user17);
        userList.add(user18);
        userList.add(user19);
        userList.add(user20);

        final int QUANTITY_ALL_USER = 50;
        Page<User> userPage = new PageImpl<>(userList, new PageRequest(NUMBER_PAGE, QUANTITY_USER_IN_PAGE, Sort.Direction.ASC, SORT_BY), QUANTITY_ALL_USER);

        return userPage;
    }
}
