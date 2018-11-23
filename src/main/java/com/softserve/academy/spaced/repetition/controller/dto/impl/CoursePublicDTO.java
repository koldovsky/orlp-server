package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

    public String getImage() {
        return linkTo(methodOn(ImageController.class).getImageById(getEntity().getImage().getId())).withSelfRel().getHref();
    }
    public Long getCreatedBy(){
        return getEntity().getCreatedBy();
    }
}
