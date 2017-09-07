package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    public static final int TOP_COURSES = 4;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DeckRepository deckRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAllByPublishedTrue();
    }

    public List<Course> getAllCoursesByCategoryId(Long category_id) {
        return courseRepository.getAllCoursesByCategoryIdAndPublishedTrue(category_id);
    }

    public List<Deck> getAllDecksByCourseId(Long category_id, Long course_id) {
        Course course = courseRepository.getCourseByCategoryIdAndId(category_id, course_id);
        return course.getDecks();
    }

    public Course getCourseById(Long category_id, Long course_id) {
        return courseRepository.getCourseByCategoryIdAndId(category_id, course_id);
    }

    public void addCourse(Course course, Long category_id) {
        Long imageId = course.getImage().getId();
        imageService.setImageStatusInUse(imageId);
        course.setCategory(new Category(category_id));
        courseRepository.save(course);
    }

    public List<Course> getTopCourse() {
        List<Course> courses = courseRepository.findCourseByOrderByRatingDesc();
        courses = courses.subList(0, TOP_COURSES);
        return courses;
    }

    public List<Course> getAllOrderedCourses() {
        return courseRepository.findAllByPublishedTrueOrderByRatingDesc();
    }

    public void updateCourse(Long course_id, Course course) {
        course.setId(course_id);
        courseRepository.save(course);
    }

    public void deleteGlobalCourse(Long course_id) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Set<Course> courses = user.getCourses();
        for (Course course : courses) {
            if (course.getId() == course_id) {
                course.setPublished(false);
                courses.remove(course);
                break;
            }
        }

        userRepository.save(user);
        courseRepository.delete(course_id);
    }

    public Course updateListOfCoursesOfTheAuthorizedUser(Long courseId) throws NotAuthorisedUserException {
        Course course = courseRepository.findOne(courseId);
        User user = userService.getAuthorizedUser();
        if (user.getCourses().contains(course)) {
            user.getCourses().remove(course);
        } else {
            user.getCourses().add(course);
        }
        userRepository.save(user);
        return course;
    }

    public List<Long> getAllCoursesIdOfTheCurrentUser() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Set<Course> listOfCourses = userService.getAllCoursesByUserId(user.getId());
        List<Long> listOfId = new ArrayList<>();
        for (Course course : listOfCourses) {
            listOfId.add(course.getId());
        }
        return listOfId;
    }

    public void createPrivateCourse(Course privateCourse, Long category_id) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Image image = imageRepository.findImageById(privateCourse.getImage().getId());
        Course course = new Course();
        course.setName(privateCourse.getName());
        course.setDescription(privateCourse.getDescription());
        course.setImage(image);
        course.setCategory(categoryRepository.findById(category_id));
        course.setPublished(false);
        course.setOwner(user);
        courseRepository.save(course);

        user.getCourses().add(course);
        userRepository.save(user);
    }


    public void updateCourseAccess(Long course_id, Course courseAccess) {
        Course course = courseRepository.findOne(course_id);
        course.setPublished(courseAccess.isPublished());
        courseRepository.save(course);
    }

    public void deleteLocalCourse(Long course_id) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Set<Course> courses = user.getCourses();
        for (Course course : courses) {
            if (course.getId() == course_id) {
                courses.remove(course);
                break;
            }
        }

        userRepository.save(user);
    }

    public void addDeckToCourse(Long course_id, Long deck_id) {
        Course course = courseRepository.findOne(course_id);
        course.getDecks().add(deckRepository.getDeckById(deck_id));
        courseRepository.save(course);
    }
}
