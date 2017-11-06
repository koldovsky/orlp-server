package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.DTO.impl.CommentDTO;
import com.softserve.academy.spaced.repetition.controller.CourseCommentController;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CourseCommentRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.service.validators.CommentFieldsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CourseCommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentService.class);

    private final CourseCommentRepository commentRepository;

    private final CourseRepository courseRepository;

    private final UserService userService;

    private final CommentFieldsValidator commentFieldsValidator;

    @Autowired
    public CourseCommentService(CourseCommentRepository commentRepository, CourseRepository courseRepository
            , UserService userService, CommentFieldsValidator commentFieldsValidator) {
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.commentFieldsValidator = commentFieldsValidator;
    }


    public CourseComment addCommentForCourse(Long courseId, String commentText, Long parentCommentId) throws NotAuthorisedUserException {
        LOGGER.debug("Added new comment for course with id: {}", courseId);
        commentFieldsValidator.validate(commentText);
        CourseComment comment = new CourseComment(commentText, new Date());
        comment.setPerson(userService.getAuthorizedUser().getPerson());
        comment.setCourse(courseRepository.findOne(courseId));
        if (parentCommentId != null) {
            comment.setParentCommentId(parentCommentId);
        }
        return commentRepository.save(comment);
    }

    public CourseComment getCommentById(Long commentId) {
        LOGGER.debug("View comment with id {}", commentId);
        return commentRepository.findOne(commentId);
    }

    public List<Comment> getAllCommentsForCourse(Long courseId) {
        LOGGER.debug("View all comments for course with id: {}", courseId);
        return commentRepository.findCourseCommentsByCourseId(courseId);
    }

    public CourseComment updateCommentById(Long commentId, String commentText) {
        LOGGER.debug("Updated courseComment with id: {}", commentId);
        commentFieldsValidator.validate(commentText);
        CourseComment updatedComment = commentRepository.findOne(commentId);
        updatedComment.setCommentDate(new Date());
        updatedComment.setCommentText(commentText);
        return updatedComment;
    }

    public void deleteCommentById(Long commentId) {
        LOGGER.debug("Deleted comment with id:{}", commentId);
        if (commentRepository.findOne(commentId).getParentCommentId() == null) {
            commentRepository.findCourseCommentsByParentCommentId(commentId).stream()
                    .forEach(comment -> commentRepository.delete(comment.getId()));
        }
        commentRepository.delete(commentId);
    }

    public List<CommentDTO> getTreeOfComments(List<CommentDTO> comments){
        LOGGER.debug("Built tree of all comments");
        Map<Long, CommentDTO> parentComments = new HashMap<>();
        for (CommentDTO comment : comments) {
            if (comment.getParentCommentId() == null) {
                parentComments.put(comment.getCommentId(), comment);
            }
        }
        for (CommentDTO childComment : comments) {
            CommentDTO parentComment = parentComments.get(childComment.getParentCommentId());
            if (parentComment != null) {
                parentComment.setChildComments(childComment);
            }
        }
        List<CommentDTO> commentsTree = new ArrayList<>();
        for (CommentDTO comment : parentComments.values()) {
            commentsTree.add(comment);
        }
        return commentsTree;
    }
}
