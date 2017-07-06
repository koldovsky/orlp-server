package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.Link;

@JsonSerialize(as=CoursePublic.class)
public interface CoursePublic {
    String getName();
    String getDescription();
}
