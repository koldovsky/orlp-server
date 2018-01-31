package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import org.springframework.hateoas.Link;

public class CourseRatingPublicDTO extends DTO<CourseRating> {

    public CourseRatingPublicDTO(CourseRating courseRating,Link link){
        super(courseRating,link);
    }

    public String getAccountEmail(){
        return getEntity().getAccountEmail();
    }

    public long getCourseId(){
        return getEntity().getCourse().getId();
    }
    public int getRating(){
        return getEntity().getRating();
    }
}
