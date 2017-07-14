package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.CourseController;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.hateoas.Link;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


public class CategoryPublicDTO extends DTO<Category> {

    public CategoryPublicDTO(Category category, Link parentLink) {
        super(category, parentLink);
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

    @Transactional
    public List<CourseLinkDTO> getLinkCourses() {

        List<CourseLinkDTO> result = new ArrayList<>();
        Link collectionLink = linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(getEntity().
                getId())).withRel("courses");
        try {
        List<Course> courses = getEntity().getCourses();
         result = DTOBuilder.buildDtoListForCollection(courses, CourseLinkDTO.class, collectionLink);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
