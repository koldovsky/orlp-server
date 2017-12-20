package com.softserve.academy.spaced.repetition.dto;

import com.softserve.academy.spaced.repetition.service.validators.ValidationConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RatingDTO {
    @Min(value = 1, message = "<1")    //
    @Max(value = 5, message = ">5")  //will be replaced
    private Integer rating;

    public RatingDTO() {
    }


    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
