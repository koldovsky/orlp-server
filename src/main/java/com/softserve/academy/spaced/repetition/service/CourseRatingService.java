package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.exceptions.MoreThanOneTimeRateException;
import com.softserve.academy.spaced.repetition.repository.CourseRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CourseRatingService {

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CardRatingService cardRatingService;

    public void addCourseRating(CourseRating courseRating, Long courseId) throws MoreThanOneTimeRateException {

        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user==null)
            System.out.println();

        String username = user.getUsername();

        CourseRating CourseRatingByAccountEmail = courseRatingRepository.findAllByAccountEmailAndCourseId(username, courseId);

           if (CourseRatingByAccountEmail == null) {

        Course course = courseRepository.findOne(courseId);
        courseRating.setAccountEmail(username);
        courseRating.setCourseId(courseId);
        courseRatingRepository.save(courseRating);

        double courseAvarageRating = cardRatingService.countAvarageRating(courseRatingRepository.findRatingByCourseId(courseId));
        System.out.println("courseAvgRate="+courseAvarageRating);
        long numbOfUsersRatings = courseRatingRepository.countAllByCourseId(courseId);
        course.setRating(courseAvarageRating);
        course.setNumbOfUsersRatings(numbOfUsersRatings);
        courseRepository.save(course);

        } else {
            throw new MoreThanOneTimeRateException();
        }
    }
}
