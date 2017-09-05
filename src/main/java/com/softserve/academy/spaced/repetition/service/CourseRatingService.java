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

    public void addCourseRating(CourseRating courseRating, Long courseId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        CourseRating courseRatingByAccountEmail = courseRatingRepository.findAllByAccountEmailAndCourseId(email, courseId);

        if (courseRatingByAccountEmail != null) {
            courseRating.setId(courseRatingByAccountEmail.getId());
        }
        Course course = courseRepository.findOne(courseId);
        courseRating.setAccountEmail(email);
        courseRating.setCourseId(courseId);
        courseRatingRepository.save(courseRating);

        double courseAverageRating = courseRatingRepository.findRatingByCourseId(courseId);
        course.setRating(courseAverageRating);
        courseRepository.save(course);
    }

    public CourseRating getCourseRatingById(Long courseId) {
        return courseRatingRepository.findOne(courseId);
    }
}
