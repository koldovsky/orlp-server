package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.domain.Image;

public class CategoryDTO {

    private String name;
    private String description;
    private Image image;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
