package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.spaced.repetition.domain.HATEOASSupport;

public abstract class DTO<T> extends HATEOASSupport {
    private T entity;

    public DTO(T entity) {
        this.entity = entity;
    }

    @JsonIgnore
    protected T getEntity() {
        return this.entity;
    }
}
