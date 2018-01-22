package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.CourseCommentRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.service.impl.CourseCommentServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CourseCommentServiceTest {

    private static final Long COURSE_ID = 1L;

    @Mock
    private CourseCommentServiceImpl courseCommentServiceUnderTest;

    @Mock
    private CourseCommentRepository courseCommentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserService mockedUserService;
    private CourseComment savedComment;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = DomainFactory.createUser(1L, new Account(), new Person(), new Folder(), null);
        savedComment = courseCommentServiceUnderTest.addCommentForCourse(COURSE_ID, "Very interesting", null);

        when(mockedUserService.getAuthorizedUser()).thenReturn(user);
    }

//
//    @Test
//    public void testAddCommentForCourse() throws Exception {
//        when(courseRepository.findOne(COURSE_ID)).thenReturn(new Course());
//        when(courseCommentRepository.save(new CourseComment()).
//
//        assertEquals("Checking field commentText", "Very interesting", savedComment.getCommentText());
//        assertEquals("Checking field parentCommentId", null, savedComment.getParentCommentId());
//        assertEquals("Checking set person", createMockedUser().getPerson(), savedComment.getPerson());
//        assertEquals("Checking set courseId", COURSE_ID, savedComment.getCourse().getId());
//    }
//
//    @Test
//    public void testGetCommentById() throws Exception {
//        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
//        CourseComment savedComment = courseCommentServiceUnderTest.addCommentForCourse(COURSE_ID, "Very interesting", null);
//        assertEquals("Getting comment for course.", savedComment, courseCommentServiceUnderTest.getCommentById(savedComment.getId()));
//    }
//
//    @Test
//    public void testGetAllCommentsForCourse() throws Exception {
//        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
//        CourseComment savedFirstComment = courseCommentServiceUnderTest.addCommentForCourse(COURSE_ID, "Good", null);
//        CourseComment savedSecondComment = courseCommentServiceUnderTest.addCommentForCourse(COURSE_ID, "Very interesting", null);
//        List<Comment> listOfCommentsForCourse = courseCommentServiceUnderTest.getAllCommentsForCourse(COURSE_ID);
//        assertEquals("Getting all comments for course.", 2, listOfCommentsForCourse.size());
//        assertEquals("Check first comment", savedFirstComment, listOfCommentsForCourse.get(0));
//        assertEquals("Check second comment", savedSecondComment, listOfCommentsForCourse.get(1));
//    }
//
//    @Test
//    public void testDeleteCommentById() throws Exception {
//        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
//        CourseComment savedComment = courseCommentServiceUnderTest.addCommentForCourse(COURSE_ID, "Very interesting", null);
//        courseCommentServiceUnderTest.addCommentForCourse(COURSE_ID, "Very interesting", savedComment.getId());
//        courseCommentServiceUnderTest.deleteCommentById(savedComment.getId());
//        assertEquals("Trying to find comment.", 0, courseCommentRepository.findCourseCommentsByCourseId(COURSE_ID).size());
//    }
//
//    @Test
//    public void testUpdateCommentById() throws Exception {
//        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
//        CourseComment savedComment = courseCommentServiceUnderTest.addCommentForCourse(COURSE_ID, "Very interesting", null);
//        CourseComment updatedComment = courseCommentServiceUnderTest.updateCommentById(savedComment.getId(), "New CommentText");
//        assertEquals("Changed comment text.", "New CommentText", updatedComment.getCommentText());
//    }

}
