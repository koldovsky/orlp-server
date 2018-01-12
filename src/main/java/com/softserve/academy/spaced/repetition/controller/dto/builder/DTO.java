package com.softserve.academy.spaced.repetition.controller.dto.builder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

public abstract class DTO<T extends EntityInterface> extends ResourceSupport {

    private static final String URL_DELIMETER = "/";

    @JsonIgnore
    private T entity;

    @JsonIgnore
    private Link selfLink;

    public DTO(T entity) {
        this.entity = entity;
    }

    public DTO(T entity, Link link) {
        this.entity = entity;
        this.selfLink = link;
        add(selfLink);
    }

    @JsonIgnore
    protected T getEntity() {
        return this.entity;
    }

    @JsonIgnore
    public Link getLinkWithReplacedParentPart(Link link) {
        String[] template = link.getHref().split(URL_DELIMETER);
        String[] self = selfLink.getHref().split(URL_DELIMETER);
        if (self.length >= template.length) {
            return link;
        }
        for (int i = 0; i < self.length; i++) {
            template[i] = self[i];
        }
        link = new Link(String.join(URL_DELIMETER, template));
        return link;
    }
}
