package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;


public interface CourseRatingService {

    void addCourseRating(int rating, Long courseId) throws NotAuthorisedUserException, UserStatusException;

    CourseRating getCourseRatingById(Long id);

}
