package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.CommentDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.ReplyToCommentDTO;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.DeckCommentService;
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
public class DeckCommentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeckCommentController.class);

    @Autowired
    private DeckCommentService commentService;

    @Auditable(action = AuditingAction.VIEW_ALL_COMMENTS_FOR_DECK)
    @GetMapping(value = "/api/category/{categoryId}/deck/{deckId}/comments")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForDeck(@PathVariable Long categoryId,
                                                                  @PathVariable Long deckId) {
        LOGGER.debug("View all comments for deck with id: {}", deckId);
        List<Comment> commentsList = commentService.getAllCommentsForDeck(deckId);
        Link collectionLink = linkTo(methodOn(DeckCommentController.class)
                .getAllCommentsForDeck(categoryId, deckId)).withSelfRel();
        List<CommentDTO> listOfComments = DTOBuilder
                .buildDtoListForCollection(commentsList, CommentDTO.class, collectionLink);
        List<CommentDTO> commentsTree = CommentDTO.buildCommentsTree(listOfComments);
        return new ResponseEntity<>(commentsTree, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_COMMENT_FOR_DECK)
    @GetMapping(value = "/api/category/{categoryId}/deck/{deckId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long categoryId,
                                                     @PathVariable Long deckId,
                                                     @PathVariable Long commentId) {
        LOGGER.debug("View comment with id {} for deck with id: {}", commentId, deckId);
        DeckComment comment = commentService.getCommentById(commentId);
        Link selfLink = linkTo(methodOn(DeckCommentController.class)
                .getCommentById(categoryId, deckId, commentId)).withSelfRel();
        CommentDTO commentDTO = DTOBuilder.buildDtoForEntity(comment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_COMMENT_FOR_DECK)
    @PostMapping(value = "/api/category/{categoryId}/deck/{deckId}/comment")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#categoryId)")
    public ResponseEntity<CommentDTO> addCommentForDeck(@Validated @RequestBody ReplyToCommentDTO replyToCommentDTO,
                                                        @RequestBody String commentText,
                                                        @PathVariable Long categoryId,
                                                        @PathVariable Long deckId) throws NotAuthorisedUserException {
        LOGGER.debug("Added comment to deck with id: {}", deckId);
        DeckComment comment = commentService
                .addCommentForDeck(deckId, replyToCommentDTO.getCommentText(), replyToCommentDTO.getParentCommentId());
        Link selfLink = linkTo(methodOn(DeckCommentController.class)
                .getCommentById(categoryId, deckId, comment.getId())).withSelfRel();
        CommentDTO commentDTO = DTOBuilder.buildDtoForEntity(comment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_COMMENT_FOR_DECK)
    @PutMapping(value = "/api/category/{categoryId}/deck/{deckId}/comment/{commentId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToUpdateCommentForDeck(#commentId)")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody String commentText,
                                                    @PathVariable Long categoryId,
                                                    @PathVariable Long deckId,
                                                    @PathVariable Long commentId) {
        LOGGER.debug("Updated comment with id: {}", commentId);
        DeckComment comment = commentService.updateCommentById(commentId, commentText);
        Link selfLink = linkTo(methodOn(DeckCommentController.class)
                .getCommentById(categoryId, deckId, commentId)).withSelfRel();
        CommentDTO commentDTO = DTOBuilder.buildDtoForEntity(comment, CommentDTO.class, selfLink);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_COMMENT_FOR_DECK)
    @DeleteMapping(value = "/api/category/{categoryId}/deck/{deckId}/comment/{commentId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeleteCommentForDeck(#commentId)")
    public ResponseEntity deleteComment(@PathVariable Long commentId, @PathVariable Long deckId) {
        LOGGER.debug("Deleted comment with id:{} for deck with id: {}", commentId, deckId);
        commentService.deleteCommentById(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
