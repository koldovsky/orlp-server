package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class DeckDTO {

    @NotNull
    private String name;

    @NotNull
    private String descpription;
    private String syntaxToHighlight;

    @Min(1)
    private Integer price;

    public DeckDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescpription() {
        return descpription;
    }

    public void setDescpription(String descpription) {
        this.descpription = descpription;
    }

    public String getSyntaxToHighlight() {
        return syntaxToHighlight;
    }

    public void setSyntaxToHighlight(String syntaxToHighlight) {
        this.syntaxToHighlight = syntaxToHighlight;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
