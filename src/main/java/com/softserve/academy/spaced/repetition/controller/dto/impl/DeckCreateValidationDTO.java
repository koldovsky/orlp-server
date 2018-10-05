package com.softserve.academy.spaced.repetition.controller.dto.impl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DeckCreateValidationDTO {

    @NotNull
    @Size(min = 2, max = 50)
    @Pattern(regexp = "[a-zA-Z+#\\s]+")
    private String name;

    @NotNull
    @Size(min = 10, max = 200)
    @Pattern(regexp = "[a-zA-Z+#.,\\s]+")
    private String description;

    @NotNull
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
