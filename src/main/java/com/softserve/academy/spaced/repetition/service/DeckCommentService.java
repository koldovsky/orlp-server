package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.exceptions.EmptyCommentTextException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckCommentRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.validators.CommentFieldsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class DeckCommentService {

    @Autowired
    private DeckCommentRepository commentRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentFieldsValidator commentFieldsValidator;

    public DeckComment addCommentForDeck(String commentText, Long deckId) throws NotAuthorisedUserException, EmptyCommentTextException {
        commentFieldsValidator.validate(commentText);
        DeckComment comment = new DeckComment(commentText, new Date());
        comment.setPerson(userService.getAuthorizedUser().getPerson());
        comment.setDeck(deckRepository.findOne(deckId));
        return commentRepository.save(comment);
    }

    public DeckComment getCommentById(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    public List<Comment> getAllCommentsForDeck(Long deckId) {
        return commentRepository.findDeckCommentsByDeckId(deckId);
    }

    @Transactional
    public DeckComment updateCommentById(Long commentId, String commentText) throws EmptyCommentTextException {
        commentFieldsValidator.validate(commentText);
        DeckComment updatedComment = commentRepository.findOne(commentId);
        updatedComment.setCommentDate(new Date());
        updatedComment.setCommentText(commentText);
        return updatedComment;
    }

    public void deleteCommentById(Long commentId) {
        commentRepository.delete(commentId);
    }
}
