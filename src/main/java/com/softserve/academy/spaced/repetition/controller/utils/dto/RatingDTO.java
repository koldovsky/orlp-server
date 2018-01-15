package com.softserve.academy.spaced.repetition.controller.utils.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MAX_COURSE_AND_CARD_RATING;
import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MIN_COURSE_AND_CARD_RATING;

public class RatingDTO {
    @Min(message = "{message.ratingMinValue}", value = MIN_COURSE_AND_CARD_RATING, groups = Request.class)
    @Max(message = "{message.ratingMaxValue}", value = MAX_COURSE_AND_CARD_RATING, groups = Request.class)
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
