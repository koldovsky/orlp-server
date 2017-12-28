package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();

    List<Course> getAllCoursesByCategoryId(Long category_id);

    List<Deck> getAllDecksByCourseId(Long category_id, Long course_id);

    Course getCourseById(Long category_id, Long course_id);

    void addCourse(Course course, Long category_id);

    List<Course> getTopCourse();

    List<Course> getAllOrderedCourses();

    void updateCourse(Long course_id, Course course);

    void deleteGlobalCourse(Long course_id) throws NotAuthorisedUserException;

    Course updateListOfCoursesOfTheAuthorizedUser(Long courseId) throws NotAuthorisedUserException;

    List<Long> getAllCoursesIdOfTheCurrentUser() throws NotAuthorisedUserException;

    void createPrivateCourse(Course privateCourse, Long category_id) throws NotAuthorisedUserException;

    void updateCourseAccess(Long course_id, Course courseAccess);

    void deleteLocalCourse(Long course_id) throws NotAuthorisedUserException;

    void addDeckToCourse(Long courseId, Long deckId);

    Page<Course> getPageWithCourses(int pageNumber, String sortBy, boolean ascending);

    Page<Course> getPageWithCoursesByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending);
  
}
