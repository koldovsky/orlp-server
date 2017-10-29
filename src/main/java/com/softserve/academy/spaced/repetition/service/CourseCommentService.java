package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CourseCommentRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.service.validators.CommentFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CourseCommentService {

    private final CourseCommentRepository commentRepository;

    private final CourseRepository courseRepository;

    private final UserService userService;

    private final CommentFieldsValidator commentFieldsValidator;

    @Autowired
    public CourseCommentService(CourseCommentRepository commentRepository, CourseRepository courseRepository, UserService userService, CommentFieldsValidator commentFieldsValidator) {
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.commentFieldsValidator = commentFieldsValidator;
    }


    public CourseComment addCommentForCourse(String commentText, Long courseId) throws NotAuthorisedUserException {
        commentFieldsValidator.validate(commentText);
        CourseComment comment = new CourseComment(commentText, new Date());
        comment.setPerson(userService.getAuthorizedUser().getPerson());
        comment.setCourse(courseRepository.findOne(courseId));
        return commentRepository.save(comment);
    }

    public CourseComment getCommentById(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    public List<Comment> getAllCommentsForCourse(Long courseId) {
        return commentRepository.findCourseCommentsByCourseId(courseId);
    }

    @Transactional
    public CourseComment updateCommentById(Long commentId, String commentText) {
        commentFieldsValidator.validate(commentText);
        CourseComment updatedComment = commentRepository.findOne(commentId);
        updatedComment.setCommentDate(new Date());
        updatedComment.setCommentText(commentText);
        return updatedComment;
    }

    public void deleteCommentById(Long commentId) {
        commentRepository.delete(commentId);
    }
}
