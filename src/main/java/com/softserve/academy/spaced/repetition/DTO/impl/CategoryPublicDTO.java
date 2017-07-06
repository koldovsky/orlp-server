package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.CategoryPublic;
import com.softserve.academy.spaced.repetition.domain.Category;

/**
 * Created by askol on 7/5/2017.
 */
public class CategoryPublicDTO extends Category implements CategoryPublic {

    public CategoryPublicDTO() {
    }

//    public CategoryPublicDTO(Long id, String name, String description) {
//        super(id, name, description);
//    }

    public CategoryPublicDTO(Category category) {
        super(category.getId(), category.getName(), category.getDescription(), category.getLink());




    }
}
