package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.CourseDTO;
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

    private static final int QUANTITY_COURSES_IN_PAGE = 12;
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
    CourseOwnershipRepository courseOwnershipRepository;
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAllByPublishedTrue();
    }

    @Override
    public List<Course> getAllCoursesByCategoryId(Long categoryId) {
        return courseRepository.getAllCoursesByCategoryIdAndPublishedTrue(categoryId);
    }

    @Override
    public List<Deck> getAllDecksByCourseId(Long categoryId, Long courseId) {
        Course course = courseRepository.getCourseByCategoryIdAndId(categoryId, courseId);
        return course.getDecks();
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.getCourseById(courseId);
    }

    @Override
    public void addCourse(Course course, Long categoryId) {
        Long imageId = course.getImage().getId();
        imageService.setImageStatusInUse(imageId);
        course.setCategory(new Category(categoryId));
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
    @Transactional
    public Course updateCourse(Long courseId, CourseDTO courseDTO) {
        Course course = courseRepository.findOne(courseId);
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setImage(courseDTO.getImage());
        updateCoursePrice(courseDTO.getPrice(), courseId);
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteGlobalCourse(Long courseId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Set<Course> courses = user.getCourses();
        for (Course course : courses) {
            if (course.getId() == courseId) {
//                course.setPublished(false);
                courses.remove(course);
                user.setCourses(courses);
                break;
            }
        }
        userRepository.save(user);
        courseRepository.delete(courseId);
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
    @Transactional
    public Course createPrivateCourse(Course privateCourse, Long categoryId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Image image = imageRepository.findImageById(privateCourse.getImage().getId());
        Course course = new Course();
        course.setName(privateCourse.getName());
        course.setDescription(privateCourse.getDescription());
        course.setImage(image);
        course.setCategory(categoryRepository.findById(categoryId));
        course.setPublished(false);
        course.setOwner(user);
        courseRepository.save(course);
        user.getCourses().add(course);
        userRepository.save(user);
        return course;
    }

    @Override
    public Course updateCourseAccess(Long courseId, Course courseAccess) {
        Course course = courseRepository.findOne(courseId);
        course.setPublished(courseAccess.isPublished());
        courseRepository.save(course);
        return course;
    }

    @Override
    public void deleteLocalCourse(Long courseId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Set<Course> courses = user.getCourses();
        for (Course course : courses) {
            if (course.getId() == courseId) {
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
        courseOwnershipRepository.deleteCourseOwnershipByCourseId(courseId);
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
    @Transactional
    public void deleteDeckFromCourse(Long courseId, Long deckId) {
        Course course = courseRepository.findOne(courseId);
        List<Deck> decks = course.getDecks();
        for (Deck deck : decks) {
            if (deck.getId() == deckId) {
                decks.remove(deck);
                break;
            }
        }
        courseRepository.save(course);
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

    @Override
    @Transactional
    public void updateCoursePrice(Integer price, Long courseId) {
        Course course = courseRepository.findOne(courseId);
        if (price == null) {
            course.setCoursePrice(null);
        } else if (course.getCoursePrice() == null) {
            CoursePrice coursePrice = new CoursePrice();
            coursePrice.setCourse(course);
            coursePrice.setPrice(price);
            course.setCoursePrice(coursePrice);
        } else if (course.getCoursePrice() != null) {
            CoursePrice coursePrice = course.getCoursePrice();
            coursePrice.setPrice(price);
        }
        courseRepository.save(course);
    }
}
