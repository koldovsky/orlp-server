package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=DeckPublic.class)
public interface CoursePublic {
    String getName();
    String getDescription();
}
