package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CourseRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseRatingService {

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    public void addCourseRating(int rating, Long courseId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        CourseRating courseRating = courseRatingRepository.findAllByAccountEmailAndCourse_Id(email, courseId);
        if (courseRating == null) {
            courseRating = new CourseRating();
        }
        Course course = courseRepository.findOne(courseId);
        courseRating.setAccountEmail(email);
        courseRating.setCourse(course);
        courseRating.setRating(rating);
        courseRatingRepository.save(courseRating);
        double courseAverageRating = courseRatingRepository.findRatingByCourse_Id(courseId);
        course.setRating(courseAverageRating);
        courseRepository.save(course);
    }

    public CourseRating getCourseRatingById(Long courseId) {
        return courseRatingRepository.findOne(courseId);
    }
}
