package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import java.util.List;

public interface DeckCommentService {

    DeckComment addCommentForDeck(Long deckId, String commentText, Long parentCommentId)
            throws NotAuthorisedUserException;

    DeckComment getCommentById(Long commentId);

    List<Comment> getAllCommentsForDeck(Long deckId);

    DeckComment updateCommentById(Long commentId, String commentText);

    void deleteCommentById(Long commentId);
}
