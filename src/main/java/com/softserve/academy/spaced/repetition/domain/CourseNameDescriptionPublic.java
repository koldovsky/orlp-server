package com.softserve.academy.spaced.repetition.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonSerialize(as=CourseNameDescriptionPublic.class)
public interface CourseNameDescriptionPublic {
    String getName();

    String getDescription();
}
