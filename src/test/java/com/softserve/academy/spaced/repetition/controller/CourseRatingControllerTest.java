package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.CourseRatingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Locale;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CourseRatingControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CourseRatingController courseRatingController;

    @Mock
    private CourseRatingService courseRatingService;
    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;
    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        final String MESSAGE_SOURCE_MESSAGE = "message";

        mockMvc = MockMvcBuilders.standaloneSetup(courseRatingController)
                .setControllerAdvice(exceptionHandlerController)
                .build();

        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class)))
                .thenReturn(MESSAGE_SOURCE_MESSAGE);
    }

    @Test
    public void getCourseRatingById() throws Exception {
        when(courseRatingService.getCourseRatingById(eq(77L))).thenReturn(createCourseRating());
        mockMvc.perform(get("/api/courses/{courseId}/ratings/{id}",5L, 77L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"rating\":3,\"accountEmail\":\"email@email\",\"courseId\":5,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost/api/courses/5/ratings/77\"}]}"));
        verify(courseRatingService).getCourseRatingById(eq(77L));
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
        final Course course = new Course();
        course.setId(5L);
        final CourseRating courseRating = new CourseRating();
        courseRating.setRating(3);
        courseRating.setCourse(course);
        courseRating.setAccountEmail("email@email");

        when(courseRatingService.addCourseRating(eq(3), eq(5L))).thenReturn(courseRating);
        mockMvc.perform(post("/api/courses/{courseId}/ratings", 5L)
                .content("{\"rating\":3,\"accountEmail\":\"email@email\",\"course\":{\"id\":5}}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(courseRatingService).addCourseRating(eq(3), eq(5L));
        verify(courseRatingService, times(1)).addCourseRating(eq(3), eq(5L));
    }

    @Test
    public void testNotAuthorizedAddCourseRating() throws Exception {
        when(courseRatingService.addCourseRating(eq(3), eq(5L))).thenThrow(new NotAuthorisedUserException());

        mockMvc.perform(post("/api/courses/{courseId}/ratings", 5L)
                .content("{\"rating\":3,\"accountEmail\":\"email@email\",\"courseId\":5}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        verify(courseRatingService).addCourseRating(eq(3), eq(5L));
        verify(messageSource).getMessage(any(String.class), any(Object[].class), any(Locale.class));
    }

    @Test
    public void testNegativeAddCourseRating() throws Exception {
        mockMvc.perform(post("/api/courses/{courseId}/ratings", 5L)
                .content("{\"ratings\": \"0\" }")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/courses/{courseId}/ratings", 5L)
                .content("{\"ratings\": \"6\" }")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
