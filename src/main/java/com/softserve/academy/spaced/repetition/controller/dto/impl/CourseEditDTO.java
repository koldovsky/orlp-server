package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CourseEditDTO extends DTO<Course> {

    public CourseEditDTO(Course entity, Link link) {
        super(entity, link);
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

    public ImageDTO getImage() {
        Link link = new Link(linkTo(methodOn(ImageController.class).getImageById(getEntity().getImage().getId())).withSelfRel().getHref());
        return new ImageDTO(getEntity().getImage(), link);
    }

    public Boolean isPublished() {
        return getEntity().isPublished();
    }

    public Long getCategoryId() {
        return getEntity().getCategory().getId();
    }

    public Integer getCoursePrice() {
        return getEntity().getEntityPrice();
    }
}
