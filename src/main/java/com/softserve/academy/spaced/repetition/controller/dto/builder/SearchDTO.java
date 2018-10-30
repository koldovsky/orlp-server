package com.softserve.academy.spaced.repetition.controller.dto.builder;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;
import org.springframework.hateoas.Link;

public abstract class SearchDTO<T extends EntityInterface> extends DTO<T> {

    private String name;

    private String description;

    private String image;

    private String resultType;

    private String selfLink;

    public SearchDTO(T entity, Link link) {
        super(entity, link);
    }

    public SearchDTO(T entity) {
        super(entity);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getResultType() {
        return resultType;
    }

    public String getSelfLink() {
        return selfLink;
    }
}
