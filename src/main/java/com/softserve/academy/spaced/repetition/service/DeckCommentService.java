package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

import java.util.List;

/**
 * This interface works with the comments to decks.
 */
public interface DeckCommentService {
    /**
     * Adds the comment to deck or answer to comment if @param parentCommentId is not null.
     *
     * @param deckId          deck`s id, must not be {@literal null}.
     * @param commentText     text of the comment, must not be {null}.
     * @param parentCommentId id of the comment on which the answer is given.
     * @return the comment or answer to comment.
     * @throws NotAuthorisedUserException if unauthorized user writes comments.
     */
    DeckComment addCommentForDeck(Long deckId, String commentText, Long parentCommentId)
            throws NotAuthorisedUserException;

    /**
     * Gets the comment with the given identifier.
     *
     * @param commentId identifier of the comment, must not be {@literal null}.
     * @return the comment with the given identifier.
     */
    DeckComment getCommentById(Long commentId);

    /**
     * Gives all of the comments to the deck with the given identifier.
     *
     * @param deckId deck`s id for which comments are searched, must not be {@literal null}.
     * @return list of comments to the deck with the given identifier.
     */
    List<Comment> getAllCommentsForDeck(Long deckId);

    /**
     * Updates comment with the given identifier by adding the new text.
     *
     * @param commentId   comment's id which will be updated, must not be {@literal null}.
     * @param commentText new text which will be set, must not be {null}.
     * @return updated comment.
     */

    DeckComment updateCommentById(Long commentId, String commentText);

    /**
     * Deletes comment from the deck with the given identifier.
     *
     * @param commentId comment's id which will be deleted, must not be {@literal null}
     */

    void deleteCommentById(Long commentId);
}
