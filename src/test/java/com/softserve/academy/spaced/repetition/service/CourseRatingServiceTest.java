package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.CourseRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.service.impl.CourseRatingServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CourseRatingServiceTest {

    private final String EMAIL = "account@test.com";
    private final Long COURSE_RATING_ID = 1L;
    private final Long COURSE_ID = 1L;
    private final int RATING_OF_COURSE_RATING = 1;
    @Mock
    private CourseRatingRepository courseRatingRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CourseRatingServiceImpl courseRatingService;
    private User user;
    private Course course;
    private CourseRating courseRating;

    @Before
    public void setUp() {
        courseRating = DomainFactory.createCourseRating(COURSE_RATING_ID, EMAIL, null, RATING_OF_COURSE_RATING);
        Account account = DomainFactory.createAccount(1L, "", EMAIL
                , null, null, true, new Date(), null, null
                , 10, null);
        course = DomainFactory.createCourse(COURSE_ID, null, null, null, 1L, true
                , null, null, null, null, null);
        user = DomainFactory.createUser(COURSE_RATING_ID, account, new Person(), new Folder(), null);
    }

    @Test
    public void testAddCourseRating() throws NotAuthorisedUserException, UserStatusException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        doNothing().when(userService).isUserStatusActive(user);
        when(courseRatingRepository.findAllByAccountEmailAndCourseId(EMAIL, COURSE_ID)).thenReturn(courseRating);
        when(courseRepository.findOne(COURSE_ID)).thenReturn(course);
        when(courseRatingRepository.save(courseRating)).thenReturn(courseRating);
        when(courseRatingRepository.findAverageRatingByCourseId(COURSE_ID)).thenReturn(1.0);

        courseRatingService.addCourseRating(RATING_OF_COURSE_RATING, COURSE_RATING_ID);
        verify(userService).getAuthorizedUser();
        verify(userService).isUserStatusActive(user);
        verify(courseRatingRepository).findAllByAccountEmailAndCourseId(EMAIL, COURSE_RATING_ID);
        verify(courseRepository).findOne(COURSE_RATING_ID);
        verify(courseRatingRepository).save(courseRating);
        verify(courseRatingRepository).findAverageRatingByCourseId(COURSE_RATING_ID);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddCourseRatingByUnauthorisedUser() throws NotAuthorisedUserException, UserStatusException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        courseRatingService.addCourseRating(RATING_OF_COURSE_RATING, COURSE_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = UserStatusException.class)
    public void testAddDeckRatingByUserWithNotActiveStatus() throws NotAuthorisedUserException, UserStatusException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        doThrow(UserStatusException.class).when(userService).isUserStatusActive(user);

        courseRatingService.addCourseRating(RATING_OF_COURSE_RATING, COURSE_ID);
        verify(userService).getAuthorizedUser();
        verify(userService).isUserStatusActive(user);
    }

    @Test
    public void testGetCourseRatingById() {
        when(courseRatingRepository.findOne(COURSE_RATING_ID)).thenReturn(courseRating);

        CourseRating result = courseRatingService.getCourseRatingById(COURSE_RATING_ID);
        verify(courseRatingRepository).findOne(COURSE_RATING_ID);
        assertEquals(courseRating, result);
    }
}
