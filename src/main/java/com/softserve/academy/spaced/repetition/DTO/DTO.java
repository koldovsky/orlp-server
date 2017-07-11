package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.spaced.repetition.domain.HATEOASSupport;
import org.springframework.hateoas.ResourceSupport;

public abstract class DTO<T extends EntityInterface> extends ResourceSupport {

    @JsonIgnore
    private T entity;

    public DTO(T entity) {
        this.entity = entity;
    }

    @JsonIgnore
    protected T getEntity() {
        return this.entity;
    }
}
