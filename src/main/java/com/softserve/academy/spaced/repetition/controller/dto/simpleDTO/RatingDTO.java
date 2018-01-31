package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MAX_COURSE_AND_CARD_RATING;
import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MIN_COURSE_AND_CARD_RATING;

public class RatingDTO {
    @Min(value = MIN_COURSE_AND_CARD_RATING, message = "{message.validation.ratingMinValue}", groups = Request.class)
    @Max(value = MAX_COURSE_AND_CARD_RATING, message = "{message.validation.ratingMaxValue}", groups = Request.class)
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
