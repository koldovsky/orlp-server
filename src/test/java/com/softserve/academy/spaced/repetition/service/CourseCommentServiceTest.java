package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.CourseCommentRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.service.impl.CourseCommentServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CourseCommentServiceTest {

    private final Long COMMENT_ID = 1L;
    private final Long COURSE_ID = 1L;
    private final String COMMENT_TEXT = "Some text";
    @InjectMocks
    private CourseCommentServiceImpl courseCommentService;
    @Mock
    private CourseCommentRepository commentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    private User user;

    @Before
    public void setUp() {
        user = DomainFactory.createUser(1L, null, new Person(), new Folder(), null);

        when(commentRepository.findOne(COMMENT_ID)).thenReturn(new CourseComment());
    }

    @Test
    public void addCommentForCourse() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(courseRepository.findOne(COURSE_ID)).thenReturn(new Course());
        when(commentRepository.save(new CourseComment())).thenReturn(new CourseComment());

        CourseComment result = courseCommentService.addCommentForCourse(COURSE_ID, COMMENT_TEXT, null);
        verify(userService).getAuthorizedUser();
        verify(courseRepository).findOne(COURSE_ID);
        verify(commentRepository).save(new CourseComment());
        assertNotNull(result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void addCommentForCourseByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        courseCommentService.addCommentForCourse(COURSE_ID, COMMENT_TEXT, null);
    }

    @Test
    public void getCommentById() {
        CourseComment result = courseCommentService.getCommentById(COMMENT_ID);
        verify(commentRepository).findOne(COMMENT_ID);
        assertNotNull(result);
    }

    @Test
    public void getAllCommentsForCourse() {
        when(commentRepository.findCourseCommentsByCourseId(COURSE_ID)).thenReturn(new ArrayList<>());

        List<Comment> result = courseCommentService.getAllCommentsForCourse(COURSE_ID);
        verify(commentRepository).findCourseCommentsByCourseId(COURSE_ID);
        assertNotNull(result);
    }

    @Test
    public void updateCommentById() {
        CourseComment result = courseCommentService.updateCommentById(COMMENT_ID, COMMENT_TEXT);
        verify(commentRepository).findOne(COMMENT_ID);
        assertNotNull(result);
    }

    @Test
    public void deleteCommentById() {
        doNothing().when(commentRepository).deleteComment(COMMENT_ID);

        courseCommentService.deleteCommentById(COMMENT_ID);
        verify(commentRepository).deleteComment(COMMENT_ID);
    }
}
