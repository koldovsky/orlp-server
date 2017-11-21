package com.softserve.academy.spaced.repetition.dto.impl;

import com.softserve.academy.spaced.repetition.dto.DTO;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.dto.Request;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.Link;

public class ReplyToCommentDTO {


    @NotBlank(message = "Can not be blank")
    private String commentText;
    private Long parentCommentId;

    public String getCommentText() {
        return commentText;
    }

    public Long getParentCommentId() { return parentCommentId; }
}
