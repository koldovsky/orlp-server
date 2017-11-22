package com.softserve.academy.spaced.repetition.dto.impl;

public class ReplyToCommentDTO {

    private String commentText;
    private Long parentCommentId;

    public String getCommentText() {
        return commentText;
    }

    public Long getParentCommentId() { return parentCommentId; }
}
