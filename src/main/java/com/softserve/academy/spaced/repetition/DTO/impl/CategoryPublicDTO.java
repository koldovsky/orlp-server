package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.CourseController;
import com.softserve.academy.spaced.repetition.domain.Category;
import org.springframework.hateoas.Link;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CategoryPublicDTO extends DTO<Category> {

    public CategoryPublicDTO(Category category, Link parentLink) {
        super(category, parentLink);
    }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public String getImagebase64() {
        return getEntity().getImagebase64();
    }

    public List<CourseLinkDTO> getCourses() {
        List<CourseLinkDTO> result = new ArrayList<>();
        Link collectionLink = linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(getEntity().
                getId())).withRel("course");
        try {
            result.addAll(DTOBuilder.buildDtoListForCollection(getEntity().getCourses(), CourseLinkDTO.class, collectionLink));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
