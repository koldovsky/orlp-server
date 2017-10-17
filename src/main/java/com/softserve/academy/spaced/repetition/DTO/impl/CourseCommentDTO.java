package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.domain.Person;
import org.springframework.hateoas.Link;

import java.util.Date;

public class CourseCommentDTO extends DTO<CourseComment> {

    public CourseCommentDTO(CourseComment courseComment, Link link) {
        super(courseComment, link);
    }

    public Long getCourseCommentId(){return getEntity().getId();}

    public String getCommentText() {return getEntity().getCommentText();}

    public Date getCommentDate() {return getEntity().getCommentDate();}

    public String getPersonFirstName(){return getEntity().getPerson().getFirstName();}

    public String getPersonLastName(){return getEntity().getPerson().getLastName();}

}
