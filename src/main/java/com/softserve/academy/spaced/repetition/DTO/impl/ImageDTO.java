package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.hateoas.Link;

public class ImageDTO extends DTO<Image> {
    public ImageDTO(Image entity, Link link) {
        super(entity, link);
    }

    public Long getImageId() {
        return getEntity().getId();
    }

}
