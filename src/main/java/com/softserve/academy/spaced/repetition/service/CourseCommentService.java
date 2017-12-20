package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CourseCommentRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Service
public class CourseCommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentService.class);

    private final CourseCommentRepository commentRepository;

    private final CourseRepository courseRepository;

    private final UserService userService;


    @Autowired
    public CourseCommentService(CourseCommentRepository commentRepository, CourseRepository courseRepository
            , UserService userService) {
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.userService = userService;
    }

    @Transactional
    public CourseComment addCommentForCourse(Long courseId, String commentText, Long parentCommentId) throws NotAuthorisedUserException {
        LOGGER.debug("Added new comment for course with id: {}", courseId);
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

    @Transactional
    public CourseComment updateCommentById(Long commentId, String commentText) {
        LOGGER.debug("Updated courseComment with id: {}", commentId);
        CourseComment updatedComment = commentRepository.findOne(commentId);
        updatedComment.setCommentDate(new Date());
        updatedComment.setCommentText(commentText);
        commentRepository.save(updatedComment);
        return updatedComment;
    }

    @Transactional
    public void deleteCommentById(Long commentId) {
        LOGGER.debug("Deleted comment with id:{}", commentId);
        commentRepository.deleteComment(commentId);
    }
}
