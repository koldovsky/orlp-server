package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CourseTopDTO extends DTO<Course> {
    public CourseTopDTO(Course course, Link link) {
        super(course, link);
        add(linkTo(methodOn(DeckController.class).getAllDecksByCourseId(getEntity().getCategory().getId(), getEntity().getId())).withRel("decks"));
    }

    public Long getCourseId(){
        return getEntity().getId();
    }

    public double getRating(){
        return getEntity().getRating();
    }

    public String getName() {
        return getEntity().getName();
    }

    public String getImage() {
        return linkTo(methodOn(ImageController.class).getImageById(getEntity().getImage().getId())).withSelfRel().getHref();
    }
}
