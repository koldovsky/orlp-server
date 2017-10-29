package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.CourseCommentRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.service.validators.CommentFieldsValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class CourseCommentServiceTest {

    private static final long COURSE_ID = 1L;

    private CourseCommentService courseCommentServiceUnderTest;

    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private UserService mockedUserService;

    @Mock
    private CommentFieldsValidator mockedCommentFieldsValidator;

    @Before
    public void setUp() throws Exception {
        courseCommentServiceUnderTest = new CourseCommentService(courseCommentRepository, courseRepository, mockedUserService, mockedCommentFieldsValidator);
    }

    private User createMockedUser() {
        User mockedUser = new User(new Account("user@email.com"), new Person("Ivan", "Kruk"), new Folder());
        mockedUser.getPerson().setId(1l);
        return mockedUser;
    }

    @Test
    public void testAddCommentForCourse() throws Exception {
        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
        CourseComment savedComment = courseCommentServiceUnderTest.addCommentForCourse("Very interesting", COURSE_ID);
        assertNotNull(savedComment);
    }

    @Test
    public void testGetCommentById() throws Exception {
        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
        CourseComment savedComment = courseCommentServiceUnderTest.addCommentForCourse("Very interesting", COURSE_ID);
        assertNotNull(courseCommentServiceUnderTest.getCommentById(savedComment.getId()));
    }

    @Test
    public void testGetAllCommentsForCourse() throws Exception {
        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
        courseCommentServiceUnderTest.addCommentForCourse("Good", COURSE_ID);
        courseCommentServiceUnderTest.addCommentForCourse("Very interesting", COURSE_ID);
        List<Comment> listOfCommentsForCourse = courseCommentServiceUnderTest.getAllCommentsForCourse(COURSE_ID);
        assertEquals("Getting all comments for course.", 2, listOfCommentsForCourse.size());
    }

    @Test
    public void testDeleteCommentById() throws Exception {
        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
        CourseComment savedComment = courseCommentServiceUnderTest.addCommentForCourse("Very interesting", COURSE_ID);
        courseCommentServiceUnderTest.deleteCommentById(savedComment.getId());
        CourseComment deletedComment = courseCommentRepository.findOne(savedComment.getId());
        assertNull("Trying to find comment.", deletedComment);
    }

    @Test
    public void testUpdateCommentById() throws Exception {
        when(mockedUserService.getAuthorizedUser()).thenReturn(createMockedUser());
        CourseComment savedComment = courseCommentServiceUnderTest.addCommentForCourse("Very interesting", COURSE_ID);
        CourseComment newComment = courseCommentServiceUnderTest.updateCommentById(savedComment.getId(), "New CommentText");
        assertEquals("Changed comment text.", newComment.getCommentText(), "New CommentText");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCommentForCourseException() throws Exception {
        doThrow(IllegalArgumentException.class).when(mockedCommentFieldsValidator).validate(eq(""));
        courseCommentServiceUnderTest.addCommentForCourse("", COURSE_ID);
    }
}
