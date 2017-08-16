package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
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
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    @Transactional
    public List <Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public List <Course> getAllCoursesByCategoryId(Long category_id) {
        return courseRepository.getAllCoursesByCategoryId(category_id);
    }

    @Transactional
    public List <Deck> getAllDecksByCourseId(Long category_id, Long course_id) {
        Course course = courseRepository.getCourseByCategoryIdAndId(category_id, course_id);
        return course.getDecks();
    }

    @Transactional
    public Course getCourseById(Long category_id, Long course_id) {
        return courseRepository.getCourseByCategoryIdAndId(category_id, course_id);
    }

    public void addCourse(Course course, Long category_id) {
        Long imageId = course.getImage().getId();
        imageService.setImageStatusInUse(imageId);
        course.setCategory(new Category(category_id));
        courseRepository.save(course);
    }

    public List <Course> get4Course() {
        List <Course> courses = courseRepository.findTop4ByOrderById();
        return courses;
    }

    public List<Course> getAllOrderedCourses() {
        return courseRepository.findAllByOrderByRatingDesc();
    }

    public void updateCourse(Long course_id, Course course) {
        course.setId(course_id);
        courseRepository.save(course);
    }

    public void deleteCourse(Long course_id) {
        courseRepository.delete(course_id);
    }

    public Course updateListOfCoursesOfTheAuthorizedUser(Long courseId) {
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

    public List <Long> getAllCoursesIdOfTheCurrentUser() {
        User user = userService.getAuthorizedUser();
        Set <Course> listOfCourses = userService.getAllCoursesByUserId(user.getId());
        List <Long> listOfId = new ArrayList <>();
        for (Course course : listOfCourses) {
            listOfId.add(course.getId());
        }
        return listOfId;
    }
}
