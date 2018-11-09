package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.CategoryController;
import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.controller.dto.builder.SearchDTO;
import com.softserve.academy.spaced.repetition.domain.Category;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CategorySearchDTO extends SearchDTO<Category> {
    public CategorySearchDTO(Category category) {
        super(category);
    }

    @Override
    public String getName() { return getEntity().getName(); }

    @Override
    public String getDescription() { return getEntity().getDescription(); }

    @Override
    public String getImage() {
        return linkTo(methodOn(ImageController.class).getImageById(getEntity().getId())).withSelfRel().getHref();
    }

    @Override
    public String getSelfLink() {
        return linkTo(methodOn(CategoryController.class).getCategoryById(getEntity().getId())).withSelfRel().getHref();
    }
}
