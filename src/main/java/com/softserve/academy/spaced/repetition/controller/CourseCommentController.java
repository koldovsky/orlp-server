package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CommentDTO;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.exceptions.EmptyCommentTextException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.CourseCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CourseCommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private CourseCommentService courseCommentService;

    @Auditable(action = AuditingAction.VIEW_ALL_COMMENTS_FOR_COURSE)
    @GetMapping(value = "/api/category/{categoryId}/course/{courseId}/comments")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByCourse(@PathVariable Long categoryId, @PathVariable Long courseId) {
        LOGGER.debug("View all comments for course with id: {}", courseId);
        List<Comment> courseCommentsList = courseCommentService.getAllCommentsForCourse(courseId);
        Link collectionLink = linkTo(methodOn(CourseCommentController.class).getAllCommentsByCourse(categoryId, courseId)).withSelfRel();
        List<CommentDTO> comments = DTOBuilder.buildDtoListForCollection(courseCommentsList, CommentDTO.class, collectionLink);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_COMMENT_FOR_COURSE)
    @GetMapping(value = "/api/category/{categoryId}/course/{courseId}/comments/{courseCommentId}")
    public ResponseEntity<CommentDTO> getCommentByCourse(@PathVariable Long courseId, @PathVariable Long courseCommentId) {
        LOGGER.debug("View comment with id {} for course with id: {}", courseCommentId, courseId);
        CourseComment courseComment = courseCommentService.getCommentById(courseCommentId);
        Link selfLink = linkTo(methodOn(CourseCommentController.class).getCommentByCourse(courseId, courseCommentId)).withSelfRel();
        CommentDTO courseCommentDTO = DTOBuilder.buildDtoForEntity(courseComment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(courseCommentDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_COMMENT_FOR_COURSE)
    @PostMapping(value = "/api/category/{categoryId}/course/{courseId}/comment")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCourse(#categoryId, #courseId)")
    public ResponseEntity<CommentDTO> addCommentByCourse(@RequestBody String courseContentComment, @PathVariable Long categoryId, @PathVariable Long courseId) throws NotAuthorisedUserException, EmptyCommentTextException {
        LOGGER.debug("Added comment to course with id: {}", courseId);
        CourseComment courseComment=courseCommentService.addCommentForCourse(courseContentComment, courseId);
        Link selfLink = linkTo(methodOn(CourseCommentController.class).getCommentByCourse(categoryId, courseId)).withSelfRel();
        CommentDTO courseCommentDTO = DTOBuilder.buildDtoForEntity(courseComment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(courseCommentDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_COMMENT_FOR_COURSE)
    @PutMapping(value = "/api/course/{courseId}/comment/{courseCommentId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToUpdateCommentForCourse(#courseCommentId)")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody String courseContentComment, @PathVariable Long courseId, @PathVariable Long courseCommentId) throws EmptyCommentTextException {
        LOGGER.debug("Updated courseComment with id: {}", courseCommentId);
        CourseComment courseComment=courseCommentService.updateCommentById(courseCommentId, courseContentComment);
        Link selfLink = linkTo(methodOn(CourseCommentController.class).getCommentByCourse(courseId, courseCommentId)).withSelfRel();
        CommentDTO courseCommentDTO = DTOBuilder.buildDtoForEntity(courseComment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(courseCommentDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_COMMENT_FOR_COURSE)
    @DeleteMapping(value = "/api/course/{courseId}/comment/{courseCommentId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeleteCommentForCourse(#courseCommentId)")
    public void deleteComment(@PathVariable Long courseCommentId,@PathVariable Long courseId) {
        LOGGER.debug("Deleted comment witj id:{} for course with id: {}", courseCommentId, courseId);
        courseCommentService.deleteCommentById(courseCommentId);
    }
}
