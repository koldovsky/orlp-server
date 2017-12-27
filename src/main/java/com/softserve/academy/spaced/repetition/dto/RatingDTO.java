package com.softserve.academy.spaced.repetition.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RatingDTO {
    @Min(value = 1, message = "<1")    //
    @Max(value = 5, message = ">5")  // TODO will be replaced

    private int rating;

    public RatingDTO() {
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
