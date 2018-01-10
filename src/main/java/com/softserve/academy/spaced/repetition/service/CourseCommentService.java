package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

import java.util.List;

/**
 * This interface works with the comments to courses.
 */
public interface CourseCommentService {
    /**
     * Adds the comment to course or answer to comment if @param parentCommentId is not null.
     *
     * @param courseId        must not be {@literal null}.
     * @param commentText     text of the comment, must not be {@literal null}.
     * @param parentCommentId id of the comment on which the answer is given.
     * @return the comment or answer to comment.
     * @throws NotAuthorisedUserException if user is not authorised
     */
    CourseComment addCommentForCourse(Long courseId, String commentText, Long parentCommentId)
            throws NotAuthorisedUserException;

    /**
     * Gets the course's comment with the given identifier.
     *
     * @param commentId must not be {@literal null}.
     * @return the comment with the given identifier.
     */
    CourseComment getCommentById(Long commentId);

    /**
     * Gets all comments of the course with the given identifier.
     *
     * @param courseId must not be {@literal null}.
     * @return list of comments
     */
    List<Comment> getAllCommentsForCourse(Long courseId);

    /**
     * Updates text of comment with the given identifier.
     *
     * @param commentId   must not be {@literal null}.
     * @param commentText new text which will be set, must not be {null}.
     * @return updated comment.
     */
    CourseComment updateCommentById(Long commentId, String commentText);

    /**
     * Deletes comment from the course with the given identifier
     *
     * @param commentId must not be {@literal null}
     */
    void deleteCommentById(Long commentId);
}
