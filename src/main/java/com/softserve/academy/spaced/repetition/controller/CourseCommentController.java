package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CommentDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.ReplyToCommentDTO;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import com.softserve.academy.spaced.repetition.service.CourseCommentService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/courses/{courseId}/comments")
public class CourseCommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private CourseCommentService courseCommentService;

    @Auditable(action = AuditingAction.VIEW_ALL_COMMENTS_FOR_COURSE)
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllCommentsByCourse(@PathVariable Long courseId) {
        LOGGER.debug("View all comments for course with id: {}", courseId);
        List<Comment> courseCommentsList = courseCommentService.getAllCommentsForCourse(courseId);
        Link collectionLink = linkTo(methodOn(CourseCommentController.class)
                .getAllCommentsByCourse(courseId)).withSelfRel();
        List<CommentDTO> listOfComments = DTOBuilder
                .buildDtoListForCollection(courseCommentsList,CommentDTO.class, collectionLink);
        List<CommentDTO> commentsTree = CommentDTO.buildCommentsTree(listOfComments);
        return new ResponseEntity<>(commentsTree, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_COMMENT_FOR_COURSE)
    @GetMapping(value = "/{courseCommentId}")
    public ResponseEntity<CommentDTO> getCommentByCourse(@PathVariable Long courseId,
                                                         @PathVariable Long courseCommentId) {
        LOGGER.debug("View comment with id {} for course with id: {}", courseCommentId, courseId);
        CourseComment courseComment = courseCommentService.getCommentById(courseCommentId);
        Link selfLink = linkTo(methodOn(CourseCommentController.class)
                .getCommentByCourse(courseId, courseCommentId)).withSelfRel();
        CommentDTO courseCommentDTO = DTOBuilder.buildDtoForEntity(courseComment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(courseCommentDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_COMMENT_FOR_COURSE)
    @PostMapping(value = "/comments")
    public ResponseEntity<CommentDTO> addCommentByCourse(@Validated(Request.class) @RequestBody ReplyToCommentDTO replyToCommentDTO,
                                                         @PathVariable Long courseId) throws NotAuthorisedUserException {
        LOGGER.debug("Added comment to course with id: {}", courseId);
        CourseComment courseComment = courseCommentService
                .addCommentForCourse(courseId, replyToCommentDTO.getCommentText(), replyToCommentDTO.getParentCommentId());
        Link selfLink = linkTo(methodOn(CourseCommentController.class)
                .getCommentByCourse(courseId, courseComment.getId())).withSelfRel();
        CommentDTO courseCommentDTO = DTOBuilder.buildDtoForEntity(courseComment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(courseCommentDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_COMMENT_FOR_COURSE)
    @PutMapping(value = "/{courseCommentId}")
    public ResponseEntity<CommentDTO> updateComment(@Validated(Request.class) @RequestBody String courseContentComment,
                                                    @PathVariable Long courseId,
                                                    @PathVariable Long courseCommentId) {
        LOGGER.debug("Updated courseComment with id: {}", courseCommentId);
        CourseComment courseComment = courseCommentService.updateCommentById(courseCommentId, courseContentComment);
        Link selfLink = linkTo(methodOn(CourseCommentController.class)
                .getCommentByCourse(courseId, courseCommentId)).withSelfRel();
        CommentDTO courseCommentDTO = DTOBuilder.buildDtoForEntity(courseComment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(courseCommentDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_COMMENT_FOR_COURSE)
    @DeleteMapping(value = "/{courseCommentId}")
    public ResponseEntity deleteComment(@PathVariable Long courseCommentId) {
        LOGGER.debug("Deleted comment with id:{}", courseCommentId);
        courseCommentService.deleteCommentById(courseCommentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
