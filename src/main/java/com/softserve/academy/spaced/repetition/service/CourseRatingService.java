package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.repository.CourseRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseRatingService {

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RatingCountService ratingCountService;

    public void addCourseRating(CourseRating courseRating, Long courseId) {

        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = user.getUsername();

        CourseRating courseRatingByAccountEmail = courseRatingRepository.findAllByAccountEmailAndCourseId(username, courseId);

        if (courseRatingByAccountEmail != null) {

            courseRating.setId(courseRatingByAccountEmail.getId());
        }
        Course course = courseRepository.findOne(courseId);
        courseRating.setAccountEmail(username);
        courseRating.setCourseId(courseId);
        courseRatingRepository.save(courseRating);

        double courseAvarageRating = ratingCountService.countAvarageRating(courseRatingRepository.findRatingByCourseId(courseId));
        long numbOfUsersRatings = courseRatingRepository.countAllByCourseId(courseId);
        course.setRating(courseAvarageRating);
        course.setNumbOfUsersRatings(numbOfUsersRatings);
        courseRepository.save(course);

    }

    public List<CourseRating> getAllCardRating() {
        List<CourseRating> courseRatings = courseRatingRepository.findAll();
        return courseRatings;
    }

    public CourseRating getCourseRatingById(Long courseId) {
        CourseRating courseRating = courseRatingRepository.findOne(courseId);
        return courseRating;
    }

}
