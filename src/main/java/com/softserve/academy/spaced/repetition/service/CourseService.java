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
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    public static final int TOP_COURSES = 4;

    private final static int QUANTITY_COURSES_IN_PAGE = 12;

    private CourseRepository courseRepository;

    private UserService userService;

    private UserRepository userRepository;


    private ImageServiceImpl imageServiceImpl;

    private ImageRepository imageRepository;

    private CategoryRepository categoryRepository;

    private DeckRepository deckRepository;

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setUserServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setImageServiceImpl(ImageServiceImpl imageServiceImpl) {
        this.imageServiceImpl = imageServiceImpl;
    }

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setDeckRepository(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

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
        imageServiceImpl.setImageStatusInUse(imageId);
        course.setCategory(new Category(category_id));
        courseRepository.save(course);
    }

    public List<Course> getTopCourse() {
        List<Course> courses = courseRepository.findTop4ByOrderByRating();
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

    public void addDeckToCourse(Long courseId, Long deckId) {
        Course course = courseRepository.findOne(courseId);
        if (course.getDecks().stream().anyMatch(deck -> deck.getId().equals(deckId))) {
            throw new IllegalArgumentException("Such deck already exists");
        }
        course.getDecks().add(deckRepository.getDeckById(deckId));
        courseRepository.save(course);
    }


    public Page<Course> getPageWithCourses(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_COURSES_IN_PAGE, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return courseRepository.findAll(request);
    }

    public Page<Course> getPageWithCoursesByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_COURSES_IN_PAGE, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return courseRepository.findAllByCategoryEquals(categoryRepository.findOne(categoryId), request);
    }
}
