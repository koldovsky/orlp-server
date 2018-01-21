package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
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
    private DeckCommentRepository deckCommentRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public DeckComment addCommentToDeck(Long deckId, String commentText, Long parentCommentId)
            throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Deck deck = deckRepository.findOne(deckId);
        Person person = user.getPerson();
        DeckComment comment = new DeckComment(commentText, new Date());
        comment.setPerson(person);
        comment.setDeck(deck);
        if (parentCommentId != null) {
            comment.setParentCommentId(parentCommentId);
        }
        LOGGER.debug("Added comment to deck with id: {}", deckId);
        return deckCommentRepository.save(comment);
    }

    @Override
    public DeckComment getCommentById(Long commentId) {
        LOGGER.debug("View comment with id {}", commentId);
        return deckCommentRepository.findOne(commentId);
    }

    @Override
    public List<Comment> getAllCommentsOfDeckByDeckId(Long deckId) {
        LOGGER.debug("View all comments for deck with id: {}", deckId);
        return deckCommentRepository.findDeckCommentsByDeckId(deckId);
    }

    @Override
    @Transactional
    public DeckComment updateCommentById(Long commentId, String commentText) {
        DeckComment comment = deckCommentRepository.findOne(commentId);
        comment.setCommentDate(new Date());
        comment.setCommentText(commentText);
        LOGGER.debug("Updated comment with id: {}", commentId);
        return comment;
    }

    @Override
    @Transactional
    public void deleteCommentById(Long commentId) {
        LOGGER.debug("Deleted comment with id:{}", commentId);
        deckCommentRepository.deleteComment(commentId);
    }
}
