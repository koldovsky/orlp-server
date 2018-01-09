package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This interface works with course
 */
public interface CourseService {

    /**
     * Find all published courses.
     *
     * @return list of courses
     */
    List<Course> getAllCourses();

    /**
     * Find all published courses in category with given identifier.
     *
     * @param category_id must not be {@literal null}.
     * @return list of courses
     */
    List<Course> getAllCoursesByCategoryId(Long category_id);

    /**
     * Find all decks in
     *
     * @param category_id must not be {@literal null}.
     * @param course_id   must not be {@literal null}.
     * @return list of decks
     */
    List<Deck> getAllDecksByCourseId(Long category_id, Long course_id);

    /**
     * Find user in certain category with the given identifiers of category and course.
     *
     * @param category_id category's id, must not be {@literal null}.
     * @param course_id   must not be {@literal null}.
     * @return course with given identifiers
     */
    Course getCourseById(Long category_id, Long course_id);

    /**
     * Adds course to category with the given identifier
     *
     * @param course      added course, must not be {null}.
     * @param category_id category's id,  must not be {@literal null}.
     */
    void addCourse(Course course, Long category_id);

    /**
     * Finds top courses by rating.
     *
     * @return 4 top rating courses
     */
    List<Course> getTopCourse();

    /**
     * Finds all published courses and orders it by rating in DESC order
     *
     * @return ordered courses in DESC order
     */
    List<Course> getAllOrderedCourses();

    /**
     * Updates course in category with the given identifier.
     *
     * @param course_id must not be {@literal null}.
     * @param course    updated course, must not be {null}.
     */
    void updateCourse(Long course_id, Course course);

    /**
     * Delete course from DB
     *
     * @param course_id must not be {@literal null}.
     * @throws NotAuthorisedUserException
     */
    void deleteGlobalCourse(Long course_id) throws NotAuthorisedUserException;

    /**
     * Add course if not exist and remove otherwise
     *
     * @param courseId must not be {@literal null}.
     * @return updated course
     * @throws NotAuthorisedUserException
     */
    Course updateListOfCoursesOfTheAuthorizedUser(Long courseId) throws NotAuthorisedUserException;

    /**
     * Find all course's id authorized user
     *
     * @return list of course's id
     * @throws NotAuthorisedUserException
     */
    List<Long> getAllCoursesIdOfTheCurrentUser() throws NotAuthorisedUserException;

    /**
     * Adds course that is available only for current user.
     *
     * @param privateCourse added course, must not be {null}.
     * @param category_id   must not be {@literal null}.
     * @throws NotAuthorisedUserException
     */
    void createPrivateCourse(Course privateCourse, Long category_id) throws NotAuthorisedUserException;

    /**
     * Updates access to course - change property published in course
     *
     * @param course_id    must not be {@literal null}.
     * @param courseAccess course with changed property published
     */
    void updateCourseAccess(Long course_id, Course courseAccess);

    /**
     * Deletes course from current user, but not from DB
     *
     * @param course_id must not be {@literal null}.
     * @throws NotAuthorisedUserException
     */
    void deleteLocalCourse(Long course_id) throws NotAuthorisedUserException;

    /**
     * Adds deck with given identifier to course with given identifier.
     *
     * @param courseId must not be {@literal null}.
     * @param deckId   must not be {@literal null}.
     */
    void addDeckToCourse(Long courseId, Long deckId);

    /**
     * Return sorted categories on each page.
     *
     * @param pageNumber zero-based page index.
     * @param sortBy     the properties to sort by, must not be null or empty.
     * @param ascending  the direction of sorting, if true sort by ASC otherwise DESC
     * @return sorted course on each page (by default 12 courses on each page)
     */
    Page<Course> getPageWithCourses(int pageNumber, String sortBy, boolean ascending);

    /**
     * Return sorted categories on each page for category with given identifier.
     *
     * @param categoryId must not be {@literal null}.
     * @param pageNumber zero-based page index.
     * @param sortBy     the properties to sort by, must not be null or empty.
     * @param ascending  the direction of sorting, if true sort by ASC otherwise DESC
     * @return sorted course on each page (by default 12 courses on each page)
     */
    Page<Course> getPageWithCoursesByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending);

}
