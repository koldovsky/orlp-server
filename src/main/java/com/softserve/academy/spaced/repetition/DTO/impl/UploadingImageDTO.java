package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.hateoas.Link;

public class UploadingImageDTO extends DTO<Image> {

    private Long bytesLeft;

    public UploadingImageDTO(Image entity, Link link) {
        super(entity, link);
    }

    public Long getImageId() {
        return getEntity().getId();
    }

    public Long getOwnerId() {
        return getEntity().getCreatedBy().getId();
    }

    public Long getBytesLeft() {
        return bytesLeft;
    }

    public void setBytesLeft(Long bytesLeft) {
        this.bytesLeft = bytesLeft;
    }
}
