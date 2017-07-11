package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.Link;

import java.util.List;



@JsonSerialize(as = CategoryPublic.class)
public interface CategoryPublic {

    public long getId();
    public String getName();
    public String getDescription();
    public String getImagebase64();
    public Link getLink();

}
