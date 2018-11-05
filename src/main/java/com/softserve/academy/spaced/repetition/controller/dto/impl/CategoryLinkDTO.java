package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.controller.CourseController;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.domain.Category;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CategoryLinkDTO extends DTO<Category> {
    private static final int PAGE_NUMBER = 1;
    private static final String SORT_BY = "id";
    private static final boolean ASCENDING = true;

    public CategoryLinkDTO(Category category, Link parentLink) {
        super(category, parentLink);
        add(linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(getEntity().getId(), PAGE_NUMBER, SORT_BY, ASCENDING)).withRel("courses"));
        add(linkTo(methodOn(DeckController.class).getAllDecksByCategoryId(getEntity().getId(),PAGE_NUMBER,SORT_BY,ASCENDING)).withRel("decks"));
    }

    public Long getCategoryId() {
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

    public Long getCreatedBy(){
        return getEntity().getCreatedBy();
    }
}
