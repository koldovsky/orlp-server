package com.softserve.academy.spaced.repetition.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityForOwnership {
    @NotNull
    @CreatedBy
    Long createdBy;

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }


}
