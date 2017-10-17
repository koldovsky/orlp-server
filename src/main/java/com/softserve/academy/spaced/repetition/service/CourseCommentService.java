package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CourseCommentRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CourseCommentService {

    @Autowired
    private CourseCommentRepository commentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserService userService;


    public CourseComment addCommentForCourse(String commentText, Long commentId) throws NotAuthorisedUserException {
        CourseComment comment = new CourseComment(commentText, new Date());
        comment.setPerson(userService.getAuthorizedUser().getPerson());
        comment.setCourse(courseRepository.findOne(commentId));
        return commentRepository.save(comment);
    }

    public CourseComment getCommentById(Long commentId){
        return commentRepository.findOne(commentId);
    }

    public List<CourseComment> getAllCommentsForCourse(Long courseId){
        return commentRepository.findCourseCommentsByCourseId(courseId);
    }

    @Transactional
    public CourseComment updateCommentById(Long commentId, String commentText){
        CourseComment updatedComment = commentRepository.findOne(commentId);
        updatedComment.setCommentDate(new Date());
        updatedComment.setCommentText(commentText);
        return updatedComment;
    }

    public void deleteCommentById(Long commentId){
         commentRepository.delete(commentId);
    }
}
