package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.domain.ImageType;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDTO extends DTO<Comment> {

    private List<CommentDTO> childComments;


    public CommentDTO(DeckComment comment, Link link) {
        super(comment, link);
    }

    public CommentDTO(CourseComment comment, Link link) {
        super(comment, link);
    }

    public Long getCommentId() { return getEntity().getId(); }

    public String getCommentText() { return getEntity().getCommentText(); }

    public Date getCommentDate() { return getEntity().getCommentDate(); }

    public String getPersonFirstName() { return getEntity().getPerson().getFirstName(); }

    public String getPersonLastName() { return getEntity().getPerson().getLastName(); }

    public ImageType getImageType() { return getEntity().getPerson().getTypeImage(); }

    public String getImageBase64() { return getEntity().getPerson().getImageBase64(); }

    public String getImage() {
        return getEntity().getPerson().getImage();
    }

    public Long getParentCommentId() { return getEntity().getParentCommentId(); }

    public List<CommentDTO> getListOfChildComments() { return childComments; }

    public void setChildComments(CommentDTO comment) {
        if (this.childComments == null) {
            this.childComments = new ArrayList<>();
        }
        this.childComments.add(comment);
    }


}