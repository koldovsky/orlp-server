package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.controller.CourseController;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CategoryLinkDTO extends DTO<Category> {

    public CategoryLinkDTO(Category category, Link parentLink) {
        super(category, parentLink);
        add(linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(getEntity().getId())).withRel("courses"));
        add(linkTo(methodOn(DeckController.class).getAllDecksByCategoryId(getEntity().getId())).withRel("decks"));
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
