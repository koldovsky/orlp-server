package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

public class CoursePublicDTO extends DTO<Course> {

    public CoursePublicDTO(Course course, Link link) {
        super(course, link);
    }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public String getImagebase64() {
        return getEntity().getImagebase64();
    }
}
