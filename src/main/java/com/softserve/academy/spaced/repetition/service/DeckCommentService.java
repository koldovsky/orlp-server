package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.DeckComment;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckCommentRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
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


    public DeckComment addCommentForDeck(String commentText, Long commentId) throws NotAuthorisedUserException {
        DeckComment comment = new DeckComment(commentText, new Date());
        comment.setPerson(userService.getAuthorizedUser().getPerson());
        comment.setDeck(deckRepository.findOne(commentId));
        return commentRepository.save(comment);
    }

    public DeckComment getCommentById(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    public List<DeckComment> getAllCommentsForDeck(Long deckId) {
        return commentRepository.findDeckCommentsByDeckId(deckId);
    }

    @Transactional
    public DeckComment updateCommentById(Long commentId, String commentText) {
        DeckComment updatedDeckComment = commentRepository.findOne(commentId);
        updatedDeckComment.setCommentDate(new Date());
        updatedDeckComment.setCommentText(commentText);
        return updatedDeckComment;
    }

    public void deleteCommentById(Long commentId) {
        commentRepository.delete(commentId);
    }
}
