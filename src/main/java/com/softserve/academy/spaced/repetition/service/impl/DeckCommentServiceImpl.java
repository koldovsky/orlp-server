package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckCommentRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.DeckCommentService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DeckCommentServiceImpl implements DeckCommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeckCommentServiceImpl.class);

    @Autowired
    private DeckCommentRepository commentRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public DeckComment addCommentForDeck(Long deckId, String commentText, Long parentCommentId)
            throws NotAuthorisedUserException {
        LOGGER.debug("Added comment to deck with id: {}", deckId);
        DeckComment comment = new DeckComment(commentText, new Date());
        comment.setPerson(userService.getAuthorizedUser().getPerson());
        comment.setDeck(deckRepository.findOne(deckId));
        if (parentCommentId != null) {
            comment.setParentCommentId(parentCommentId);
        }
        return commentRepository.save(comment);
    }

    @Override
    public DeckComment getCommentById(Long commentId) {
        LOGGER.debug("View comment with id {}", commentId);
        return commentRepository.findOne(commentId);
    }

    @Override
    public List<Comment> getAllCommentsForDeck(Long deckId) {
        LOGGER.debug("View all comments for deck with id: {}", deckId);
        return commentRepository.findDeckCommentsByDeckId(deckId);
    }

    @Override
    @Transactional
    public DeckComment updateCommentById(Long commentId, String commentText) {
        LOGGER.debug("Updated comment with id: {}", commentId);
        DeckComment updatedComment = commentRepository.findOne(commentId);
        updatedComment.setCommentDate(new Date());
        updatedComment.setCommentText(commentText);
        return updatedComment;
    }

    @Override
    @Transactional
    public void deleteCommentById(Long commentId) {
        LOGGER.debug("Deleted comment with id:{}", commentId);
        commentRepository.deleteComment(commentId);
    }
}
