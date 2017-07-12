package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface EntityInterface {
    @JsonIgnore
   public Long getId();
}
