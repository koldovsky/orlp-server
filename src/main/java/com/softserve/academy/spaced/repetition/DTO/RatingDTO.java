package com.softserve.academy.spaced.repetition.DTO;

public class RatingDTO {

    private int rating;

    public RatingDTO() {
    }

    public RatingDTO(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
