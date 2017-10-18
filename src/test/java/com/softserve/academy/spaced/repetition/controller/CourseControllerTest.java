package com.softserve.academy.spaced.repetition.controller;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CourseControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new ExceptionHandlerController())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void getCourseById() throws Exception {
        long categoryId = 1L;
        long courseId = 1L;
        when(courseService.getCourseById(eq(categoryId),eq(courseId))).thenReturn(createCourse());
        mockMvc.perform(get("/api/category/{category_id}/courses/{course_id}", 1, 1)
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
                        "  \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/courses/1\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/1/courses/1/decks\"}]"+
                        "}"));
    }

    private Course createCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Java interview course");
        course.setDescription("4 parts of java questions & answers");
        course.setRating(0);
        course.setPublished(true);
        User user = new User();
        Account account = new Account();
        account.setId(1L);
        user.setId(1L);
        user.setAccount(account);
        course.setOwner(user);
        Category category = new Category();
        category.setId(1L);
        course.setCategory(category);
        course.setImage(new Image(14L));
        return course;
    }

    @Test
    public void getCoursesByPage() throws Exception {
        int numberPage =1 ;
        String sortBy = "name";
        boolean ascending = true;
        when(courseService.getPageWithCourses(numberPage,sortBy,ascending)).thenReturn(createCourses());
        mockMvc.perform(get("/api/courses?p=1&sortBy=name&asc=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"content\": [" +
                        "    {" +
                        "      \"name\": \"C# interview course\"," +
                        "      \"description\": \"questions & answers\"," +
                        "      \"rating\": 0," +
                        "      \"image\": \"http://localhost/api/service/image/16\"," +
                        "      \"published\": true," +
                        "      \"ownerId\": 1," +
                        "      \"categoryId\": 3," +
                        "      \"courseId\": 3," +
                        "       \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/3/courses/3\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/3/courses/3/decks\"}]"+
                        "    }," +
                        "    {" +
                        "      \"name\": \"C++ interview course\"," +
                        "      \"description\": \"3 parts of java questions & answers\"," +
                        "      \"rating\": 0," +
                        "      \"image\": \"http://localhost/api/service/image/15\"," +
                        "      \"published\": true," +
                        "      \"ownerId\": 1," +
                        "      \"categoryId\": 2," +
                        "      \"courseId\": 2," +
                        "       \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/2/courses/2\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/2/courses/2/decks\"}]"+
                        "    }," +
                        "    {" +
                        "      \"name\": \"Java interview course\"," +
                        "      \"description\": \"4 parts of java questions & answers\"," +
                        "      \"rating\": 0," +
                        "      \"image\": \"http://localhost/api/service/image/14\"," +
                        "      \"published\": true," +
                        "      \"ownerId\": 1," +
                        "      \"categoryId\": 1," +
                        "      \"courseId\": 1," +
                        "       \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/1/courses/1\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/1/courses/1/decks\"}]"+
                        "    }," +
                        "    {" +
                        "      \"name\": \"JavaScript interview course\"," +
                        "      \"description\": \"questions & answers\"," +
                        "      \"rating\": 0," +
                        "      \"image\": \"http://localhost/api/service/image/18\"," +
                        "      \"published\": true," +
                        "      \"ownerId\": 1," +
                        "      \"categoryId\": 10," +
                        "      \"courseId\": 5," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/10/courses/5\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/10/courses/5/decks\"}]"+
                        "    }," +
                        "    {" +
                        "      \"name\": \"PHP interview course\"," +
                        "      \"description\": \"2 parts of java questions & answers\"," +
                        "      \"rating\": 0," +
                        "      \"image\": \"http://localhost/api/service/image/17\"," +
                        "      \"published\": true," +
                        "      \"ownerId\": 1," +
                        "      \"categoryId\": 4," +
                        "      \"courseId\": 4," +
                        "      \"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/4/courses/4\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/4/courses/4/decks\"}]"+
                        "    }" +
                        "  ]," +
                        "  \"last\": true," +
                        "  \"totalPages\": 1," +
                        "  \"totalElements\": 5," +
                        "  \"size\": 0," +
                        "  \"number\": 0," +
                        "  \"sort\": null," +
                        "  \"first\": true," +
                        "  \"numberOfElements\": 5" +
                        "}"));
    }

    private Page<Course> createCourses() throws ParseException {
        List<Course> courseList = new ArrayList<>();

        Course course = new Course();
        course.setId(3L);
        course.setName("C# interview course");
        course.setDescription("questions & answers");
        course.setRating(0);
        course.setImage(new Image(16L));
        course.setPublished(true);
        User user = new User();
        Account account = new Account();
        account.setId(1L);
        user.setId(1L);
        user.setAccount(account);
        course.setOwner(user);
        Category category = new Category();
        category.setId(3L);
        course.setCategory(category);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("C++ interview course");
        course2.setDescription("3 parts of java questions & answers");
        course2.setRating(0);
        course2.setImage(new Image(15L));
        course2.setPublished(true);
        course2.setOwner(user);
        Category category2 = new Category();
        category2.setId(2L);
        course2.setCategory(category2);

        Course course3 = new Course();
        course3.setId(1L);
        course3.setName("Java interview course");
        course3.setDescription("4 parts of java questions & answers");
        course3.setRating(0);
        course3.setImage(new Image(14L));
        course3.setPublished(true);
        course3.setOwner(user);
        Category category3 = new Category();
        category3.setId(1L);
        course3.setCategory(category3);

        Course course4 = new Course();
        course4.setId(5L);
        course4.setName("JavaScript interview course");
        course4.setDescription("questions & answers");
        course4.setRating(0);
        course4.setImage(new Image(18L));
        course4.setPublished(true);
        course4.setOwner(user);
        Category category4 = new Category();
        category4.setId(10L);
        course4.setCategory(category4);

        Course course5 = new Course();
        course5.setId(4L);
        course5.setName("PHP interview course");
        course5.setDescription("2 parts of java questions & answers");
        course5.setRating(0);
        course5.setImage(new Image(17L));
        course5.setPublished(true);
        course5.setOwner(user);
        Category category5 = new Category();
        category5.setId(4L);
        course5.setCategory(category5);


        courseList.add(course);
        courseList.add(course2);
        courseList.add(course3);
        courseList.add(course4);
        courseList.add(course5);

        Page<Course> coursePage = new PageImpl<>(courseList);
        return coursePage;
    }

    @Test
    public void getCoursesByPageAndCategory() throws Exception {
        int categoryId = 2;
        int numberPage =1 ;
        String sortBy = "name";
        boolean ascending = true;
        when(courseService.getPageWithCoursesByCategory(categoryId, numberPage, sortBy, ascending)).thenReturn(createCoursesBySelectedCategory());
        mockMvc.perform(get("/api/category/2/courses?p=1&sortBy=name&asc=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"name\":\"C++ interview course\",\"description\":\"3 parts of java questions & answers\",\"published\":false,\"image\":\"http://localhost/api/service/image/15\",\"rating\":0.0,\"courseId\":2,\"categoryId\":2,\"ownerId\":1,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/2/courses?pageNumber=1&sortBy=name&ascending=true\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/2/courses/2/decks\"}]}],\"last\":true,\"totalElements\":1,\"totalPages\":1,\"size\":0,\"number\":0,\"sort\":null,\"first\":true,\"numberOfElements\":1}"));
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

        Page<Course> coursePage = new PageImpl<>(courseList);
        return coursePage;
    }
}
