package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

public interface AccessToUrlService {
    boolean hasAccessToCategory(Long category_id);

    boolean hasAccessToCourse(Long category_id, Long course_id);

    boolean hasAccessToDeckFromFolder(Long folder_id, Long deckId);

    boolean hasAccessToCourse(Long category_id);

    boolean hasAccessToDeck(Long category_id, Long course_id, Long deck_id);

    boolean hasAccessToDeckFromCategory(Long category_id, Long deck_id);

    boolean hasAccessToDeck(Long category_id);

    boolean hasAccessToCard(Long deck_id, Long card_id);

    boolean hasAccessToCard(Long category_id, Long deck_id, Long card_id);

    boolean hasAccessToCard(Long category_id, Long course_id, Long deck_id, Long card_id);

    boolean hasAccessToFolder(Long folder_id) throws NotAuthorisedUserException;

    boolean hasAccessToDeleteCommentForCourse(Long commentId) throws NotAuthorisedUserException;

    boolean hasAccessToDeleteCommentForDeck(Long commentId) throws NotAuthorisedUserException;

    boolean hasAccessToUpdateCommentForDeck(Long commentId) throws NotAuthorisedUserException;

    boolean hasAccessToUpdateCommentForCourse(Long commentId) throws NotAuthorisedUserException;
}
