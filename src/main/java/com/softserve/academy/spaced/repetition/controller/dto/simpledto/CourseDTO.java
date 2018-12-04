package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.domain.Image;

public class CourseDTO {
    private String name;
    private String description;
    private Image image;
    private Integer price;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public Integer getPrice() {
        return price;
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

    public void setPrice(Integer price) {
        this.price = price;
    }
}
