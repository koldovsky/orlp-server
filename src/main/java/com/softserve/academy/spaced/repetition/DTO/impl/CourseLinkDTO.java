package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CourseLinkDTO extends DTO<Course> {

    public CourseLinkDTO(Course course, Link link) {
        super(course, link);
        add(linkTo(methodOn(DeckController.class).getAllDecksByCourseId(getEntity().getCategory().getId(), getEntity().getId())).withRel("decks"));
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
