package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.controller.CourseCommentController;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import org.springframework.hateoas.Link;

import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CourseCommentLinkDTO extends DTO<CourseComment> {

    public CourseCommentLinkDTO(CourseComment courseComment, Link link) {
        super(courseComment, link);
        add(linkTo(methodOn(CourseCommentController.class).getAllCommentsByCourse(getEntity().getCourse().getCategory()
                .getId(), getEntity().getCourse().getId())).withRel("comments"));
    }

    public Long getCourseCommentId() {
        return getEntity().getId();
    }

    public String getCommentText() {return getEntity().getCommentText();}

    public Date getCommentDate() {
        return getEntity().getCommentDate();
    }

    public String getPersonFirstName() {
        return getEntity().getPerson().getFirstName();
    }

    public String getPersonLastName() {
        return getEntity().getPerson().getLastName();
    }
}
