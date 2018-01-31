package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.ImageController;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ImageDTO extends DTO<Image> {
    public ImageDTO(Image entity, Link link) {
        super(entity, link);
        removeLinks();
        getLinks().add(linkTo(methodOn(ImageController.class).getImageById(getEntity().getId())).withSelfRel());
    }

    public Long getImageId() {
        return getEntity().getId();
    }

    public boolean getIsImageUsed() { return getEntity().getIsImageUsed(); }

}
