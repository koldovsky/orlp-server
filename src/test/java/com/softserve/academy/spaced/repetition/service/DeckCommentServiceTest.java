package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.DeckCommentRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.DeckCommentServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class DeckCommentServiceTest {

    @InjectMocks
    private DeckCommentServiceImpl deckCommentService;

    @Mock
    private DeckCommentRepository commentRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserService userService;

    private User user;
    private Person person;
    private Deck deck;
    private DeckComment deckComment;

    private final long USER_ID = 1L;
    private final long PERSON_ID = 1L;
    private final long DECK_ID = 1L;
    private final long DECK_COMMENT_ID = 1L;
    private final String DECK_COMMENT_TEXT = "Comment Text";
    private final long DECK_COMMENT_PARENT_ID = 1L;

    @Before
    public void setUp() {
        person = DomainFactory.createPerson(PERSON_ID, null, null, null, null, null);
        user = DomainFactory.createUser(USER_ID, null, person, null, null);
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0, user, null, null, null, null);
        deckComment = DomainFactory.createDeckComment(DECK_COMMENT_ID, DECK_COMMENT_TEXT, null, person,
                DECK_COMMENT_PARENT_ID, deck);

        when(commentRepository.findOne(DECK_COMMENT_ID)).thenReturn(deckComment);
    }

    @Test
    public void testAddCommentForDeck() throws NotAuthorisedUserException {
        deckComment.setId(null);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(commentRepository.save(deckComment)).thenReturn(deckComment);

        DeckComment result = deckCommentService.addCommentForDeck(DECK_ID, DECK_COMMENT_TEXT, DECK_COMMENT_PARENT_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        verify(commentRepository).save(deckComment);
        assertEquals(deckComment, result);

        deckComment.setId(DECK_COMMENT_ID);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddCommentForDeckByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        deckCommentService.addCommentForDeck(DECK_ID, DECK_COMMENT_TEXT, DECK_COMMENT_PARENT_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetCommentById() {
        DeckComment result = deckCommentService.getCommentById(DECK_COMMENT_ID);
        verify(commentRepository).findOne(DECK_COMMENT_ID);
        assertEquals(deckComment, result);
    }

    @Test
    public void testGetAllCommentsForDeck() {
        when(commentRepository.findDeckCommentsByDeckId(DECK_ID)).thenReturn(null);

        List<Comment> result = deckCommentService.getAllCommentsForDeck(DECK_ID);
        verify(commentRepository).findDeckCommentsByDeckId(DECK_ID);
        assertNull(result);
    }

    @Test
    public void testUpdateCommentById() {
        DeckComment result = deckCommentService.updateCommentById(DECK_COMMENT_ID, DECK_COMMENT_TEXT);
        verify(commentRepository).findOne(DECK_COMMENT_ID);
        assertEquals(deckComment, result);
    }

    @Test
    public void testDeleteCommentById() {
        doNothing().when(commentRepository).deleteComment(DECK_COMMENT_ID);

        deckCommentService.deleteCommentById(DECK_COMMENT_ID);
        verify(commentRepository).deleteComment(DECK_COMMENT_ID);
    }
}
