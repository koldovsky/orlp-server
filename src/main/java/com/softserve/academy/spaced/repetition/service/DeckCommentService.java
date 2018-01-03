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
     * Add a comment to decks
     *
     * @return true if operation is successful
     */
    DeckComment addCommentForDeck(Long deckId, String commentText, Long parentCommentId)
            throws NotAuthorisedUserException;

    /**
     * Returns a comment with the given identifier
     *
     * @param commentId must not be {@literal null}.
     */
    DeckComment getCommentById(Long commentId);

    /**
     * Returns all comments to the deck with the given identifier
     *
     * @param deckId must not be {@literal null}.
     * @return list of comments to the deck with the given identifier
     */
    List<Comment> getAllCommentsForDeck(Long deckId);

    /**
     * Updates comment with the given identifier with the new text
     *
     * @param commentId   must not be {@literal null}
     * @param commentText must not be {null}
     * @return true if operation is successful
     */

    DeckComment updateCommentById(Long commentId, String commentText);

    /**
     * Delete comments to the deck with the given identifier
     *
     * @param commentId must not be {@literal null}
     * @return true if operation is successful
     */

    void deleteCommentById(Long commentId);
}
