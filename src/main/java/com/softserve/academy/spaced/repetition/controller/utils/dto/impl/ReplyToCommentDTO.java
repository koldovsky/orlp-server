package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import org.hibernate.validator.constraints.NotBlank;

public class ReplyToCommentDTO {

    @NotBlank(message = "{message.validation.fieldNotEmpty}", groups = Request.class)
    private String commentText;
    private Long parentCommentId;

    public String getCommentText() {
        return commentText;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }
}
