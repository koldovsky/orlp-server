package com.softserve.academy.spaced.repetition.controller.dto.builder;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;
import org.springframework.hateoas.Link;

import java.util.Objects;


public abstract class SearchDTO<T extends EntityInterface> extends DTO<T> {

    private String name;

    private String description;

    private String image;

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

    public String getSelfLink() {
        return selfLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SearchDTO<?> searchDTO = (SearchDTO<?>) o;
        return Objects.equals(name, searchDTO.name) &&
                Objects.equals(description, searchDTO.description) &&
                Objects.equals(image, searchDTO.image) &&
                Objects.equals(selfLink, searchDTO.selfLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, image, selfLink);
    }
}
