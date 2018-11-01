package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.CourseController;
import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.controller.dto.builder.SearchDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.enums.SearchType;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CourseSearchDTO extends SearchDTO<Course> {

    public CourseSearchDTO(Course entity) {
        super(entity);
    }

    @Override
    public String getName() { return getEntity().getName(); }

    @Override
    public String getDescription() { return getEntity().getDescription(); }

    @Override
    public String getImage() {
        return linkTo(methodOn(ImageController.class).getImageById(getEntity().getImage().getId())).withSelfRel().getHref();
    }

    public double getRating() { return getEntity().getRating(); }

    @Override
    public String getResultType() {return SearchType.COURSE.toString(); }

    @Override
    public String getSelfLink() {
        return linkTo(methodOn(CourseController.class).getCourseById(getEntity().getId())).withSelfRel().getHref();
    }
}
