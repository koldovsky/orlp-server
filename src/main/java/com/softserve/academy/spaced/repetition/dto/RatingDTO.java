package com.softserve.academy.spaced.repetition.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RatingDTO {

    @Min(value = 1)
    @Max(value = 5)
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
