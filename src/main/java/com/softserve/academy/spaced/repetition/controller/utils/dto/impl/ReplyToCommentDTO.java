package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import org.hibernate.validator.constraints.NotBlank;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.EMPTY_MESSAGE;

public class ReplyToCommentDTO {

    @NotBlank(message = EMPTY_MESSAGE, groups = Request.class)
    private String commentText;
    private Long parentCommentId;

    public String getCommentText() {
        return commentText;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }
}
