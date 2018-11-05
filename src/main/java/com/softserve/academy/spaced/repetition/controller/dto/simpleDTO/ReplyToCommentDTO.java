package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
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

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
