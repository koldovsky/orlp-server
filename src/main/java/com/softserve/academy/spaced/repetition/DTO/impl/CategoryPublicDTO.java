package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.CategoryPublic;
import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import org.springframework.hateoas.Link;

import java.util.List;


public class CategoryPublicDTO extends DTO<Category> {

    public CategoryPublicDTO(Category category) {
        super(category);
    }
    public String getName(){
        return getEntity().getName();
    }
    public String getDescription(){
        return  getEntity().getDescription();
    }
    public String getImagebase64(){
        return  getEntity().getImagebase64();
    }

    public List<Link> getLinks(){
        return getEntity().getLinks();
    }

}
