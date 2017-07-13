package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

public class CourseLinkDTO extends DTO<Course> {

    public CourseLinkDTO(Course course, Link parentLink) {
        super(course, parentLink);
    }
}
