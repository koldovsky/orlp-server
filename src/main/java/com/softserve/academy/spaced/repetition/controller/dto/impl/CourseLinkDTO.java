package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CourseLinkDTO extends DTO <Course> {

    public CourseLinkDTO(Course course, Link link) {
        super(course, link);
        add(linkTo(methodOn(DeckController.class).getAllDecksByCourseId(getEntity().getCategory().getId(), getEntity().getId())).withRel("decks"));
    }

    public Long getCourseId() {
        return getEntity().getId();
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

    public Long getOwnerId() {
        return getEntity().getOwner().getId();
    }

    public Boolean isPublished() {
        return getEntity().isPublished();
    }

    public Double getRating() {
        return getEntity().getRating();
    }

    public Long getCategoryId() {
        return getEntity().getCategory().getId();
    }

    public Long getCreatedBy(){
        return getEntity().getCreatedBy();
    }
}
