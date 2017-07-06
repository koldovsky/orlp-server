package com.softserve.academy.spaced.repetition.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.Link;

import java.util.List;

/**
 * Created by Dementhor on 7/5/2017.
 */

@JsonSerialize(as = CategoryPublic.class)
public interface CategoryPublic {

    public String getName();

    public String getDescription();
    public String getImagebase64();

    public Link getLink();

}
