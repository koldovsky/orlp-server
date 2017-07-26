package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
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
        return getEntity().getCourseId();
    }
    public int getRating(){
        return getEntity().getRating();
    }
}
