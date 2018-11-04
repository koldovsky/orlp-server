package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.CourseService;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    private final static int QUANTITY_COURSES_IN_PAGE = 12;
    private final Locale locale = LocaleContextHolder.getLocale();
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
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAllByPublishedTrue();
    }

    @Override
    public List<Course> getAllCoursesByCategoryId(Long category_id) {
        return courseRepository.getAllCoursesByCategoryIdAndPublishedTrue(category_id);
    }

    @Override
    public List<Deck> getAllDecksByCourseId(Long category_id, Long course_id) {
        Course course = courseRepository.getCourseByCategoryIdAndId(category_id, course_id);
        return course.getDecks();
    }

    @Override
    public Course getCourseById(Long course_id) {
        return courseRepository.getCourseById(course_id);
    }

    @Override
    public void addCourse(Course course, Long category_id) {
        Long imageId = course.getImage().getId();
        imageService.setImageStatusInUse(imageId);
        course.setCategory(new Category(category_id));
        courseRepository.save(course);
    }

    @Override
    public List<Course> getTopCourse() {
        List<Course> courses = courseRepository.findTop4ByOrderByRating();
        return courses;
    }

    @Override
    public List<Course> getAllOrderedCourses() {
        return courseRepository.findAllByPublishedTrueOrderByRatingDesc();
    }

    @Override
    public void updateCourse(Long course_id, Course course) {
        course.setId(course_id);
        courseRepository.save(course);
    }

    @Override
    public void deleteGlobalCourse(Long course_id) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Set<Course> courses = user.getCourses();
        for (Course course : courses) {
            if (course.getId() == course_id) {
//                course.setPublished(false);
                courses.remove(course);
                user.setCourses(courses);
                break;
            }
        }

        userRepository.save(user);
        courseRepository.delete(course_id);
    }

    @Override
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

    @Override
    public List<Long> getAllCoursesIdOfTheCurrentUser() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Set<Course> listOfCourses = userService.getAllCoursesByUserId(user.getId());
        List<Long> listOfId = new ArrayList<>();
        for (Course course : listOfCourses) {
            listOfId.add(course.getId());
        }
        return listOfId;
    }

    @Override
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

    @Override
    public Course updateCourseAccess(Long course_id, Course courseAccess) {
        Course course = courseRepository.findOne(course_id);
        course.setPublished(courseAccess.isPublished());
        courseRepository.save(course);
        return course;
    }

    @Override
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

    @Transactional
    @Override
    public void deleteCourseAndSubscriptions(Long courseId) {
        courseRepository.deleteSubscriptions(courseId);
        courseRepository.delete(courseId);
    }

    @Override
    public Course addDeckToCourse(Long courseId, Long deckId) {
        Course course = courseRepository.findOne(courseId);
        if (course.getDecks().stream().anyMatch(deck -> deck.getId().equals(deckId))) {
            throw new IllegalArgumentException(messageSource.getMessage("message.exception.deckAlreadyExists",
                    new Object[]{}, locale));
        }
        course.getDecks().add(deckRepository.getDeckById(deckId));
        courseRepository.save(course);
        return course;
    }

    @Override
    public Page<Course> getPageWithCourses(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_COURSES_IN_PAGE, ascending
                ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return courseRepository.findAllByPublishedTrue(request);
    }

    @Override
    public Page<Course> getPageWithCoursesByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_COURSES_IN_PAGE, ascending
                ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return courseRepository.findAllByCategoryEqualsAndPublishedTrue(categoryRepository.findOne(categoryId), request);
    }

    @Override
    public Set<BigInteger> findCoursesId(String searchString) {
        return courseRepository.findCoursesId(searchString);
    }

    @Override
    public List<Course> findAllCoursesBySearch(String searchString) {
        return courseRepository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(searchString, searchString);
    }
}
