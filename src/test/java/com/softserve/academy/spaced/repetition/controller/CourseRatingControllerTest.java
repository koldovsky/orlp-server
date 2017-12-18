package com.softserve.academy.spaced.repetition.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.impl.CourseRatingServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CourseRatingControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CourseRatingController courseRatingController;

    @Mock
    private CourseRatingServiceImpl courseRatingService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseRatingController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void getCourseRatingById() throws Exception {
        when(courseRatingService.getCourseRatingById(eq(77L))).thenReturn(createCourseRating());
        mockMvc.perform(get("/api/course/{courseId}/rating/{id}", 5L, 77L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"rating\":3,\"accountEmail\":\"email@email\",\"courseId\":5,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/course/5/rating/77\"}]}"));
    }

    private CourseRating createCourseRating() {
        Course course = new Course();
        course.setId(5L);
        CourseRating courseRating = new CourseRating("email@email", course, 3);
        courseRating.setId(77L);
        return courseRating;
    }


    @Test
    public void testAddCourseRating() throws Exception {
        mockMvc.perform(post("/api/private/course/{courseId}", 5L)
                .content("{\"rating\":3,\"accountEmail\":\"email@email\",\"course\":{\"id\":5}}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(courseRatingService, times(1)).addCourseRating(eq(3), eq(5L));
    }

    @Test
    public void testNotAuthorizedAddCourseRating() throws Exception {
        doThrow(NotAuthorisedUserException.class).when(courseRatingService).addCourseRating(eq(3), eq(5L));

        mockMvc.perform(post("/api/private/course/{courseId}", 5L)
                .content("{\"rating\":3,\"accountEmail\":\"email@email\",\"courseId\":5}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testNegativeAddCourseRating() throws Exception {
        mockMvc.perform(post("/api/private/course/{courseId}", 5L)
                .content("{\"rating\":0}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/private/course/{courseId}", 5L)
                .content("{\"rating\":6}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
