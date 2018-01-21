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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class DeckCommentServiceTest {

    @InjectMocks
    private DeckCommentServiceImpl deckCommentService;

    @Mock
    private DeckCommentRepository deckCommentRepository;

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
        deck = DomainFactory.createDeck(DECK_ID, null, null, null, null, 0, user, null, null, null, null, null);
        deckComment = DomainFactory.createDeckComment(DECK_COMMENT_ID, DECK_COMMENT_TEXT, null, person,
                DECK_COMMENT_PARENT_ID, deck);
    }

    @Test
    public void testAddCommentToDeck() throws NotAuthorisedUserException {
        deckComment.setId(null);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(deckRepository.findOne(DECK_ID)).thenReturn(deck);
        when(deckCommentRepository.save(deckComment)).thenReturn(deckComment);

        DeckComment result = deckCommentService.addCommentToDeck(DECK_ID, DECK_COMMENT_TEXT, DECK_COMMENT_PARENT_ID);
        verify(userService).getAuthorizedUser();
        verify(deckRepository).findOne(DECK_ID);
        verify(deckCommentRepository).save(deckComment);
        assertEquals(deckComment, result);

        deckComment.setId(DECK_COMMENT_ID);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testAddCommentToDeckByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        deckCommentService.addCommentToDeck(DECK_ID, DECK_COMMENT_TEXT,DECK_COMMENT_PARENT_ID);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetCommentById() {
        when(deckCommentRepository.findOne(DECK_COMMENT_ID)).thenReturn(deckComment);

        DeckComment result = deckCommentService.getCommentById(DECK_COMMENT_ID);
        verify(deckCommentRepository).findOne(DECK_COMMENT_ID);
        assertEquals(deckComment, result);
    }

    @Test
    public void testGetAllCommentsOfDeckByDeckId() {
        when(deckCommentRepository.findDeckCommentsByDeckId(DECK_ID)).thenReturn(null);

        List<Comment> result = deckCommentService.getAllCommentsOfDeckByDeckId(DECK_ID);
        verify(deckCommentRepository).findDeckCommentsByDeckId(DECK_ID);
        assertEquals(null, result);
    }

    @Test
    public void testUpdateCommentById() {
        when(deckCommentRepository.findOne(DECK_COMMENT_ID)).thenReturn(deckComment);

        DeckComment result = deckCommentService.updateCommentById(DECK_COMMENT_ID, DECK_COMMENT_TEXT);
        verify(deckCommentRepository).findOne(DECK_COMMENT_ID);
        assertEquals(deckComment, result);
    }

    @Test
    public void testDeleteCommentById() {
        doNothing().when(deckCommentRepository).deleteComment(DECK_COMMENT_ID);

        deckCommentService.deleteCommentById(DECK_COMMENT_ID);
        verify(deckCommentRepository).deleteComment(DECK_COMMENT_ID);
    }

}
