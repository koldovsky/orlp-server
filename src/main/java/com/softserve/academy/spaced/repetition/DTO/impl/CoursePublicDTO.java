package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

import java.util.List;

public class CoursePublicDTO extends DTO<Course> {
    public CoursePublicDTO(Course course) {
        super(course);
    }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public List<Link> getLinks(){
        return getEntity().getLinks();
    }
}
