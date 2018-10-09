package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.*;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final long USER_ID = 1L;
    private MockMvc mockMvc;
    UserController userController;

    @Mock
    UserService userService;

    @Before
    public void setUp() {
        userController = new UserController();
        userController.userService = userService;
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void testGetAuthorizedUserPublicInfo() throws Exception {
        when(userService.getAuthorizedUser()).thenReturn(getUser());
        mockMvc.perform(get("/api/user/details")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", Matchers.is(1)));
    }

    @Test
    public void testGetAllCoursesByUserId() throws Exception {
        when(userService.getAllCoursesByUserId(USER_ID)).thenReturn(getListCourseLinkDTO());
        mockMvc.perform(get("/api/private/user/{userId}/courses", USER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].createdBy", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].published", Matchers.is(true)));
        verify(userService,times(1)).getAllCoursesByUserId(USER_ID);
    }

    @Test
    public void testGetAuthorizedUserWithLinks() throws Exception {
        when(userService.getAuthorizedUser()).thenReturn(getUser());
        mockMvc.perform(get("/api/user")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId",Matchers.is(1)));
        verify(userService,times(1)).getAuthorizedUser();
    }

    private Set<Course> getListCourseLinkDTO() {
        User user1 = new User();
        Category category1 = new Category();
        Course course1 = new Course();
        Set<Course> courses = new HashSet<>();
        Image image = new Image();
        image.setId(USER_ID);
        image.setType("good");
        image.setSize(5L);

        user1.setId(USER_ID);
        category1.setId(USER_ID);
        course1.setId(USER_ID);
        course1.setName("course1");
        course1.setDescription("description");
        course1.setImage(new Image());
        course1.setPublished(true);
        course1.setRating(10L);
        course1.setCreatedBy(USER_ID);
        course1.setOwner(user1);
        course1.setCategory(new Category());
        course1.setCourseRatings(new ArrayList<CourseRating>());
        course1.setCourseComments(new ArrayList<CourseComment>());
        course1.setDecks(new ArrayList<Deck>());
        courses.add(course1);
        return courses;
    }

    private User getUser() {
        Account account;
        Person person;
        User user;
        Authority authority;
        Set<Authority> authorities;
        Folder folder;

        folder = new Folder();
        folder.setId(USER_ID);
        folder.setDecks(new HashSet<>());

        authority = new Authority();
        authority.setId(USER_ID);
        authority.setName(AuthorityName.ROLE_USER);

        authorities = new HashSet<>();
        authorities.add(authority);

        account = new Account();
        account.setId(USER_ID);
        account.setEmail("aaaaa@aa.aa");
        account.setAuthenticationType(AuthenticationType.LOCAL);
        account.setStatus(AccountStatus.ACTIVE);
        account.setLearningRegime(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
        account.setCardsNumber(1);
        account.setAuthorities(authorities);
        account.setDeactivated(false);
        account.setPassword("1q2w3e4r");

        person = new Person();
        person.setFirstName("firstname");
        person.setLastName("lastname");
        person.setImageType(ImageType.NONE);
        person.setImageBase64("imagebase64");
        person.setImage("image");

        user = new User();
        user.setId(USER_ID);
        user.setAccount(account);
        user.setPerson(person);
        user.setCourses(new HashSet<>());
        user.setFolder(folder);
        return user;
    }
}

