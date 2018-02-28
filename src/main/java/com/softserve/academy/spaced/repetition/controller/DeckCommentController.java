package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CommentDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.ReplyToCommentDTO;
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

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/decks/{deckId}/comments")
public class DeckCommentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeckCommentController.class);

    @Autowired
    private DeckCommentService commentService;

    @Auditable(action = AuditingAction.VIEW_COMMENT_FOR_DECK)
    @GetMapping(value = "/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK_COMMENT','READ')")
    public CommentDTO getCommentById(@PathVariable Long deckId, @PathVariable Long commentId) {
        LOGGER.debug("View comment with id {} for deck with id: {}", commentId, deckId);
        DeckComment comment = commentService.getCommentById(commentId);
        return buildDtoForEntity(comment, CommentDTO.class,
                linkTo(methodOn(DeckCommentController.class)
                        .getCommentById(deckId, commentId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.CREATE_COMMENT_FOR_DECK)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('DECK_COMMENT','CREATE')")
    public CommentDTO addCommentForDeck(@Validated @RequestBody ReplyToCommentDTO replyToCommentDTO,
                                                        @PathVariable Long deckId) throws NotAuthorisedUserException {
        LOGGER.debug("Added comment to deck with id: {}", deckId);
        DeckComment comment = commentService
                .addCommentForDeck(deckId, replyToCommentDTO.getCommentText(), replyToCommentDTO.getParentCommentId());
        return buildDtoForEntity(comment, CommentDTO.class,
                linkTo(methodOn(DeckCommentController.class)
                        .getCommentById(deckId, comment.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.DELETE_COMMENT_FOR_DECK)
    @DeleteMapping(value = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission('DECK_COMMENT','DELETE') ||" +
            "@deckCommentServiceImpl.getCommentById(#commentId).createdBy==principal.id")
    public void deleteComment(@PathVariable Long commentId, @PathVariable Long deckId) {
        LOGGER.debug("Deleted comment with id:{} for deck with id: {}", commentId, deckId);
        commentService.deleteCommentById(commentId);
    }

    @Auditable(action = AuditingAction.VIEW_ALL_COMMENTS_FOR_DECK)
    @GetMapping
    @PreAuthorize("hasPermission('DECK_COMMENT','READ')")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForDeck(@PathVariable Long deckId) {
        LOGGER.debug("View all comments for deck with id: {}", deckId);
        List<Comment> commentsList = commentService.getAllCommentsForDeck(deckId);
        Link collectionLink = linkTo(methodOn(DeckCommentController.class)
                .getAllCommentsForDeck(deckId)).withSelfRel();
        List<CommentDTO> listOfComments = DTOBuilder
                .buildDtoListForCollection(commentsList, CommentDTO.class, collectionLink);
        List<CommentDTO> commentsTree = CommentDTO.buildCommentsTree(listOfComments);
        return new ResponseEntity<>(commentsTree, HttpStatus.OK);
    }
}
