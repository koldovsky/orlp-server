package com.softserve.academy.spaced.repetition.controller.dto.impl;

public class DeckBuyDTO {

    private boolean isBought;
    private int points;

    public DeckBuyDTO(boolean isBought, int points) {
        this.isBought = isBought;
        this.points = points;
    }

    public DeckBuyDTO() {
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean getIsBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }
}

