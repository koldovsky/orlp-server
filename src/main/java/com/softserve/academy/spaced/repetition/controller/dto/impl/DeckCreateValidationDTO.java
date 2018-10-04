package com.softserve.academy.spaced.repetition.controller.dto.impl;

import org.hibernate.validator.constraints.Length;

public class DeckCreateValidationDTO {

    @Length(min = 2, max = 50)
    private String name;

    @Length(min = 10, max = 200)
    private String description;

    private Long categoryId;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
