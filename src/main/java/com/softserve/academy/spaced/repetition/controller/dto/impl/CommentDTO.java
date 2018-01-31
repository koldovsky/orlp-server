package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.domain.enums.ImageType;
import org.springframework.hateoas.Link;

import java.util.*;

public class CommentDTO extends DTO<Comment> implements Comparable<CommentDTO> {

    private List<CommentDTO> childComments;

    public CommentDTO(DeckComment comment, Link link) {
        super(comment, link);
    }

    public CommentDTO(CourseComment comment, Link link) {
        super(comment, link);
    }

    public Long getCommentId() {
        return getEntity().getId();
    }

    public String getCommentText() {
        return getEntity().getCommentText();
    }

    public Date getCommentDate() {
        return getEntity().getCommentDate();
    }

    public Long getPersonId() {
        return getEntity().getPerson().getId();
    }

    public String getPersonFirstName() {
        return getEntity().getPerson().getFirstName();
    }

    public String getPersonLastName() {
        return getEntity().getPerson().getLastName();
    }

    public ImageType getImageType() {
        return getEntity().getPerson().getImageType();
    }

    public String getImageBase64() {
        return getEntity().getPerson().getImageBase64();
    }

    public String getImage() {
        return getEntity().getPerson().getImage();
    }

    public Long getParentCommentId() {
        return getEntity().getParentCommentId();
    }

    public List<CommentDTO> getListOfChildComments() {
        return childComments;
    }

    public void addChildComment(CommentDTO comment) {
        if (this.childComments == null) {
            this.childComments = new ArrayList<>();
        }
        this.childComments.add(comment);
    }

    public static List<CommentDTO> buildCommentsTree(List<CommentDTO> comments) {
        Map<Long, CommentDTO> parentComments = new HashMap<>();
        for (CommentDTO comment : comments) {
            if (comment.getParentCommentId() == null) {
                parentComments.put(comment.getCommentId(), comment);
            }
        }
        for (CommentDTO childComment : comments) {
            CommentDTO parentComment = parentComments.get(childComment.getParentCommentId());
            if (parentComment != null) {
                parentComment.addChildComment(childComment);
            }
        }
        List<CommentDTO> commentsTree = new ArrayList<>(parentComments.values());
        Collections.sort(commentsTree);
        return commentsTree;
    }

    @Override
    public int compareTo(CommentDTO o1) {
        return this.getCommentDate().compareTo(o1.getCommentDate());
    }
}