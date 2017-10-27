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
        Course course = createCourse(1L, "Java interview course","4 parts of java questions & answers",
                0,14L, true, 1L,1L, 1L);
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

    public Course createCourse(long idCourse, String nameCourse, String descriptionCourse, double rating,
                               long imageId, boolean isPublished, long accountId, long userId,
                               long categoryId){
        Course course = new Course();
        course.setId(idCourse);
        course.setName(nameCourse);
        course.setDescription(descriptionCourse);
        course.setRating(rating);
        course.setImage(new Image(imageId));
        course.setPublished(isPublished);
        User user = new User();
        Account account = new Account();
        account.setId(accountId);
        user.setId(userId);
        user.setAccount(account);
        course.setOwner(user);
        Category category = new Category();
        category.setId(categoryId);
        course.setCategory(category);
        return course;
    }

    private Page<Course> createCourses() throws ParseException {
        List<Course> courseList = new ArrayList<>();

        Course course = createCourse(3L,"C# interview course", "questions & answers",
                0,16L,true,1L,1L,3L);

        Course course2 = createCourse(2L,"C++ interview course", "3 parts of java questions & answers",
                0,15L,true,1L,1L,2L);

        Course course3 = createCourse(1L,"Java interview course", "4 parts of java questions & answers",
                0,14L,true,1L,1L,1L);

        Course course4 = createCourse(5L,"JavaScript interview course", "questions & answers",
                0,18L,true,1L,1L,10L);

        Course course5 = createCourse(4L,"PHP interview course", "2 parts of java questions & answers",
                0,17L,true,1L,1L,4L);

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
                .andExpect(content().json("{\"content\":[{\"name\":\"C++ interview course\",\"description\":\"3 parts of java questions & answers\",\"published\":true,\"image\":\"http://localhost/api/service/image/15\",\"rating\":0.0,\"courseId\":2,\"categoryId\":2,\"ownerId\":1,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/category/2/courses?pageNumber=1&sortBy=name&ascending=true\"},{\"rel\":\"decks\",\"href\":\"http://localhost/api/category/2/courses/2/decks\"}]}],\"last\":true,\"totalElements\":1,\"totalPages\":1,\"size\":0,\"number\":0,\"sort\":null,\"first\":true,\"numberOfElements\":1}"));
    }

    private Page<Course> createCoursesBySelectedCategory() throws ParseException {
        List<Course> courseList = new ArrayList<>();

        Course course = createCourse(2L,"C++ interview course",
                "3 parts of java questions & answers", 0,15L,true,
                1L,1L, 2L);
        courseList.add(course);

        Page<Course> coursePage = new PageImpl<>(courseList);
        return coursePage;
    }
}
