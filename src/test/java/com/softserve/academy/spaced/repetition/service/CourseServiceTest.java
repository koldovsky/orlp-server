package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.impl.CourseServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CourseServiceTest {

    private final Long COURSE_ID = 1L;
    private final Long CATEGORY_ID = 1L;
    private final Long DECK_ID = 1L;
    private final int PAGE_NUMBER = 1;
    private final boolean PAGE_ASCENDING_ORDER = true;
    private final String PAGE_SORT_BY = "field";
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private DeckRepository deckRepository;
    @InjectMocks
    private CourseServiceImpl courseService;
    private Course course;
    private User user;
    private Category category;
    private Image image;
    private List<Course> courseList;
    private Set<Course> courseSet;
    private List<Deck> deckList;

    @Before
    public void setUp() throws Exception {
        courseList = new ArrayList<>();
        courseSet = new HashSet<>();
        deckList = new ArrayList<>();

        Deck deck = DomainFactory.createDeck(DECK_ID, null, null, null, category
                , 1.0, null, null, null, null, null);
        image = DomainFactory.createImage(1L, null, null, null, null, true);
        category = DomainFactory.createCategory(CATEGORY_ID, null, null, image);
        user = DomainFactory.createUser(1L, null, null, null, courseSet);
        course = DomainFactory.createCourse(COURSE_ID, null, null, new Image(), 1, true
                , user, new Category(), deckList, null, null);

        courseList.add(course);
        courseSet.add(course);
        deckList.add(deck);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseRepository.findOne(COURSE_ID)).thenReturn(course);
    }

    @Test
    public void testGetAllCourses() {
        when(courseRepository.findAllByPublishedTrue()).thenReturn(courseList);

        List<Course> result = courseService.getAllCourses();
        verify(courseRepository).findAllByPublishedTrue();
        assertEquals(courseList, result);
    }

    @Test
    public void testGetAllCoursesByCategoryId() {
        when(courseRepository.getAllCoursesByCategoryIdAndPublishedTrue(CATEGORY_ID)).thenReturn(courseList);

        List<Course> result = courseService.getAllCoursesByCategoryId(CATEGORY_ID);
        verify(courseRepository).getAllCoursesByCategoryIdAndPublishedTrue(CATEGORY_ID);
        assertEquals(courseList, result);
    }

    @Test
    public void testGetAllDecksByCourseId() {
        when(courseRepository.getCourseByCategoryIdAndId(CATEGORY_ID, COURSE_ID)).thenReturn(course);

        List<Deck> result = courseService.getAllDecksByCourseId(CATEGORY_ID, COURSE_ID);
        verify(courseRepository).getCourseByCategoryIdAndId(CATEGORY_ID, COURSE_ID);
        assertEquals(deckList, result);
    }

    @Test
    public void testGetCourseById() {
        when(courseRepository.getCourseById(COURSE_ID)).thenReturn(course);

        Course result = courseService.getCourseById(COURSE_ID);
        verify(courseRepository).getCourseById(COURSE_ID);
        assertEquals(course, result);
    }

    @Test
    public void testAddCourse() {
        doNothing().when(imageService).setImageStatusInUse(null);

        courseService.addCourse(course, CATEGORY_ID);
        verify(imageService).setImageStatusInUse(null);
        verify(courseRepository).save(course);
    }

    @Test
    public void testGetTopCourse() {
        when(courseRepository.findTop4ByOrderByRating()).thenReturn(courseList);

        List<Course> result = courseService.getTopCourse();
        verify(courseRepository).findTop4ByOrderByRating();
        assertEquals(courseList, result);
    }

    @Test
    public void testGetAllOrderedCourses() {
        when(courseRepository.findAllByPublishedTrueOrderByRatingDesc()).thenReturn(courseList);

        List<Course> result = courseService.getAllOrderedCourses();
        verify(courseRepository).findAllByPublishedTrueOrderByRatingDesc();
        assertEquals(courseList, result);
    }

    @Test
    public void testUpdateCourse() {
        courseService.updateCourse(COURSE_ID, course);
        verify(courseRepository).save(course);
    }

    @Test
    public void testDeleteCourseByAdmin() {
        doNothing().when(courseRepository).delete(COURSE_ID);

        courseService.deleteCourseAndSubscriptions(COURSE_ID);
        verify(courseRepository).deleteSubscriptions(COURSE_ID);
        verify(courseRepository).delete(COURSE_ID);
    }

    @Test
    public void testDeleteGlobalCourse() throws NotAuthorisedUserException {
        doNothing().when(courseRepository).delete(COURSE_ID);

        courseService.deleteGlobalCourse(COURSE_ID);
        verify(userService).getAuthorizedUser();
        verify(userRepository).save(user);
        verify(courseRepository).delete(COURSE_ID);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteGlobalCourseByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        courseService.deleteGlobalCourse(COURSE_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testUpdateListOfCoursesOfTheAuthorizedUser() throws NotAuthorisedUserException {
        Course result = courseService.updateListOfCoursesOfTheAuthorizedUser(COURSE_ID);
        verify(courseRepository).findOne(COURSE_ID);
        verify(userService).getAuthorizedUser();
        verify(userRepository).save(user);
        assertEquals(course, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testUpdateListOfCoursesOfTheAuthorizedUserByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        courseService.updateListOfCoursesOfTheAuthorizedUser(COURSE_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetAllCoursesIdOfTheCurrentUser() throws NotAuthorisedUserException {
        List<Long> coursesId = new ArrayList<>();
        coursesId.add(course.getId());
        when(userService.getAllCoursesByUserId(user.getId())).thenReturn(courseSet);

        List<Long> result = courseService.getAllCoursesIdOfTheCurrentUser();
        verify(userService).getAuthorizedUser();
        verify(userService).getAllCoursesByUserId(user.getId());
        assertEquals(coursesId, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testGetAllCoursesIdOfTheCurrentUserByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        courseService.getAllCoursesIdOfTheCurrentUser();
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testCreatePrivateCourse() throws NotAuthorisedUserException {
        when(imageRepository.findImageById(course.getImage().getId())).thenReturn(image);
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(category);

        courseService.createPrivateCourse(course, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
        verify(courseRepository).save(any(Course.class));
        verify(userRepository).save(user);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCreatePrivateCourseByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        courseService.createPrivateCourse(course, CATEGORY_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testUpdateCourseAccess() {
        courseService.updateCourseAccess(COURSE_ID, course);
        verify(courseRepository).findOne(COURSE_ID);
        verify(courseRepository).save(course);
    }

    @Test
    public void testDeleteLocalCourse() throws NotAuthorisedUserException {

        courseService.deleteLocalCourse(COURSE_ID);
        verify(userService).getAuthorizedUser();
        verify(userRepository).save(user);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testDeleteLocalCourseByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        courseService.deleteLocalCourse(COURSE_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testAddDeckToCourse() {

        when(deckRepository.getDeckById(DECK_ID)).thenReturn(deckList.get(0));
        deckList.clear();

        courseService.addDeckToCourse(COURSE_ID, DECK_ID);
        verify(courseRepository).findOne(COURSE_ID);
        verify(deckRepository).getDeckById(DECK_ID);
        verify(courseRepository).save(course);
    }

    @Test
    public void testGetPageWithCourses() {
        when(courseRepository.findAllByPublishedTrue(any(PageRequest.class))).thenReturn(null);
        Page<Course> result = courseService.getPageWithCourses(PAGE_NUMBER, PAGE_SORT_BY, PAGE_ASCENDING_ORDER);
        verify(courseRepository).findAllByPublishedTrue(any(PageRequest.class));
        assertNull(result);
    }

    @Test
    public void testGetPageWithCoursesByCategory() {
        when(courseRepository.findAllByCategoryEqualsAndPublishedTrue(any(Category.class), any(PageRequest.class)))
                .thenReturn(null);

        Page<Course> result = courseService.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER
                , PAGE_SORT_BY, PAGE_ASCENDING_ORDER);
        verify(courseRepository).findAllByCategoryEqualsAndPublishedTrue(any(Category.class), any(PageRequest.class));
        assertNull(result);
    }
}
