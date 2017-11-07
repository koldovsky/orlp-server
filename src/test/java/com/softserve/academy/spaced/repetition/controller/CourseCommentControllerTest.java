/*
package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.ReplyDTO;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.service.CourseCommentService;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CourseCommentControllerTest {

    private MockMvc mockMvc;
    private static final Date COMMENT_DATE = new Date(1508281622000L);
    private static final long CATEGORY_ID = 1L;
    private static final long COURSE_ID = 1L;
    private static final long COMMENT_ID = 3L;

    @InjectMocks
    private CourseCommentController courseCommentController;

    @Mock
    private CourseCommentService courseCommentService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseCommentController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();

    }

    private CourseComment createCourseComment(String personFirstName, String personLastName, String commentText, Long commentId) {
        Person person = new Person(personFirstName, personLastName);
        CourseComment courseComment = new CourseComment(commentText, COMMENT_DATE);
        courseComment.setId(commentId);
        courseComment.setPerson(person);
        return courseComment;
    }

    private List<Comment> createListOfComments() {
        List<Comment> listOfComments = new ArrayList<>();
        listOfComments.add(createCourseComment("Admin", "Admin", "Good course", 3l));
        listOfComments.add(createCourseComment("Petro", "Kruk", "Not bad",1l));
        listOfComments.add(createCourseComment("Stepan", "Shyba", "Cool", 2l));
        return listOfComments;
    }

    @Test
    public void getCommentByCourse() throws Exception {
        when(courseCommentService.getCommentById(eq(COMMENT_ID)))
                .thenReturn(createCourseComment("Admin", "Admin", "Good course", COMMENT_ID));
        mockMvc.perform(get("/api/category/{categoryId}/course/{courseId}/comments/{courseCommentId}"
                , CATEGORY_ID, COURSE_ID, COMMENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"personLastName\": \"Admin\"," +
                        "\"personFirstName\": \"Admin\"," +
                        "\"courseCommentId\": 3," +
                        "\"commentText\": \"Good course\"," +
                        "\"commentDate\": 1508281622000," +
                        "\"links\":[{\"rel\":\"self\",\"href\": \"http://localhost/api/category/1/course/1/comments/3\"}]}"));
    }

    @Test
    public void getAllCommentsByCourse() throws Exception{
        when(courseCommentService.getAllCommentsForCourse(eq(COURSE_ID))).thenReturn(createListOfComments());
        mockMvc.perform(get("/api/category/{categoryId}/course/{courseId}/comments"
                , CATEGORY_ID, COURSE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{" +
                        "\"personLastName\": \"Admin\"," +
                        "\"personFirstName\": \"Admin\"," +
                        "\"courseCommentId\": 3," +
                        "\"commentText\": \"Good course\"," +
                        "\"commentDate\": 1508281622000," +
                        "\"links\":[{\"rel\":\"self\",\"href\": \"http://localhost/api/category/1/course/1/comments/3\"}]},"+
                        "{" +
                        "\"personLastName\": \"Kruk\"," +
                        "\"personFirstName\": \"Petro\"," +
                        "\"courseCommentId\": 1," +
                        "\"commentText\": \"Not bad\"," +
                        "\"commentDate\": 1508281622000," +
                        "\"links\":[{\"rel\":\"self\",\"href\": \"http://localhost/api/category/1/course/1/comments/1\"}]},"+
                        "{" +
                        "\"personLastName\": \"Shyba\"," +
                        "\"personFirstName\": \"Stepan\"," +
                        "\"courseCommentId\": 2," +
                        "\"commentText\": \"Cool\"," +
                        "\"commentDate\": 1508281622000," +
                        "\"links\":[{\"rel\":\"self\",\"href\": \"http://localhost/api/category/1/course/1/comments/2\"}]}]"
                ));
    }

    @Test
    public void updateComment() throws Exception {
        when(courseCommentService.updateCommentById(eq(COMMENT_ID), eq("Comment")))
                .thenReturn(createCourseComment("Admin", "Admin", "Good course",COMMENT_ID));
        mockMvc.perform(put("/api/category/{categoryId}/course/{courseId}/comment/{courseCommentId}"
                , CATEGORY_ID, COURSE_ID, COMMENT_ID)
                .content("Comment")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/category/{categoryId}/course/{courseId}/comment/{courseCommentId}"
                , CATEGORY_ID, COURSE_ID, COMMENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
*/
/*
    @Test
    public void testAddComment() throws Exception {
        when(courseCommentService.addCommentForCourse(eq(COURSE_ID), eq("Comment"), eq(null)))
                .thenReturn(createCourseComment("Admin", "Admin", "Good course",COMMENT_ID));
        mockMvc.perform(post("/api/category/{categoryId}/course/{courseId}/comment"
                , CATEGORY_ID, COURSE_ID)
                .content("Comment")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }*//*

}*/
