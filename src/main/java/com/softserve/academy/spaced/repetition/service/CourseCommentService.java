package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

import java.util.List;

public interface CourseCommentService {
    CourseComment addCommentForCourse(Long courseId, String commentText, Long parentCommentId)
            throws NotAuthorisedUserException;

    CourseComment getCommentById(Long commentId);

    List<Comment> getAllCommentsForCourse(Long courseId);

    CourseComment updateCommentById(Long commentId, String commentText);

    void deleteCommentById(Long commentId);
}
