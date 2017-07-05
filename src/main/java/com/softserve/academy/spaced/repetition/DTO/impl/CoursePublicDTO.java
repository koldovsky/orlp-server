package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.CoursePublic;
import com.softserve.academy.spaced.repetition.domain.Course;

public class CoursePublicDTO extends Course implements CoursePublic {
    public CoursePublicDTO(Course course) {
        super(course.getName(), course.getDescription(), course.getCategory(), course.getDecks());
    }
}
