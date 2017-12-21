package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.service.CourseService;
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
public class CourseControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    final int NUMBER_PAGE = 1;
    final String SORT_BY = "name";
    final int QUANTITY_COURSES_IN_PAGE = 12;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void getCourseById() throws Exception {
        final long categoryId = 1L;
        final long courseId = 1L;
        when(courseService.getCourseById(eq(categoryId), eq(courseId))).thenReturn(createCourse());
        mockMvc.perform(get("/api/category/{category_id}/courses/{course_id}", categoryId, courseId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"name\": \"Java interview course\"," +
                        "  \"description\": \"4 parts of java questions & answers\"," +
                        "  \"rating\": 0," +
                        "  \"image\": \"http://localhost/api/service/image/14\"," +
                        "  \"published\": true," +
                        "  \"ownerId\": 1," +
                        "  \"categoryId\": 1," +
                        "  \"courseId\": 1," +
                        "  \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/courses/1\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/1/courses/1/decks\"}]" +
                        "}"));
    }

    public Course createCourse(long idCourse, String nameCourse,
                               String descriptionCourse, int rating,
                               long imageId, boolean isPublished,
                               long accountId, String accountEmail, int ownerId, long categoryId) {
        Course course = new Course();
        course.setName(nameCourse);
        course.setDescription(descriptionCourse);
        course.setId(idCourse);
        course.setRating(rating);
        course.setImage(new Image(imageId));
        course.setPublished(isPublished);

        User user = new User();
        Account account = new Account();
        account.setId(accountId);
        account.setEmail(accountEmail);
        user.setId(ownerId);
        user.setAccount(account);
        course.setOwner(user);

        Category category = new Category();
        category.setId(categoryId);
        course.setCategory(category);
        return course;
    }

    private Course createCourse() {
        Course course = createCourse(1L, "Java interview course", "4 parts of java questions & answers",
                0, 14L, true, 1L, "admin@gmail.com", 1, 1L);
        return course;
    }

    @Test
    public void getCoursesByPage() throws Exception {
        when(courseService.getPageWithCourses(NUMBER_PAGE, SORT_BY, true)).thenReturn(createCourses());
        mockMvc.perform(get("/api/courses?p=" + NUMBER_PAGE + "&sortBy=" + SORT_BY + "&asc=" + true)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(3)))
                .andExpect(jsonPath("$.size", is(QUANTITY_COURSES_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(true)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(false)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(SORT_BY)));
    }

    private Page<Course> createCourses() throws ParseException {
        List<Course> courseList = new ArrayList<>();
        final int quantityCourses = 25;
        for (int i = 1; i <= quantityCourses; i++) {
            Course course = createCourse(i, "C# interview course", "questions & answers",
                    0, 16L, true, 1L, "admin@gmail.com", 1, i);
            courseList.add(course);
        }

        Page<Course> coursesPage = new PageImpl<>(courseList, new PageRequest(NUMBER_PAGE - 1, QUANTITY_COURSES_IN_PAGE, Sort.Direction.ASC, SORT_BY), quantityCourses);

        return coursesPage;
    }

    @Test
    public void getCoursesByPageAndCategory() throws Exception {
        final int categoryId = 2;
        when(courseService.getPageWithCoursesByCategory(categoryId, 1, "name", true)).thenReturn(createCoursesBySelectedCategory());
        mockMvc.perform(get("/api/category/" + categoryId + "/courses?p=" + NUMBER_PAGE + "&sortBy=" + SORT_BY + "&asc=" + true)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(QUANTITY_COURSES_IN_PAGE)))
                .andExpect(jsonPath("$.sort[:1].ascending", hasItem(true)))
                .andExpect(jsonPath("$.sort[:1].descending", hasItem(false)))
                .andExpect(jsonPath("$.sort[:1].property", hasItem(SORT_BY)));
    }

    private Page<Course> createCoursesBySelectedCategory() throws ParseException {
        List<Course> courseList = new ArrayList<>();

        Course course = new Course();
        course.setName("C++ interview course");
        course.setDescription("3 parts of java questions & answers");
        course.setId(2L);
        course.setRating(0);
        course.setImage(new Image(15L));

        User user = new User();
        Account account = new Account();
        account.setId(1L);
        account.setEmail("admin@gmail.com");
        user.setId(1L);
        user.setAccount(account);
        course.setOwner(user);

        Category category = new Category();
        category.setId(2L);
        course.setCategory(category);

        courseList.add(course);

        Page<Course> coursesPage = new PageImpl<>(courseList, new PageRequest(NUMBER_PAGE - 1, QUANTITY_COURSES_IN_PAGE, Sort.Direction.ASC, SORT_BY), courseList.size());

        return coursesPage;
    }
}
