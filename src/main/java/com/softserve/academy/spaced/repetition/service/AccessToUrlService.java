package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

/**
 * Create some kind of security for application.
 */
public interface AccessToUrlService {
    /**
     * Checks on availability of category with the given identifier in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCategory(Long categoryId);

    /**
     * Checks on availability of course in category with the given identifiers in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @param courseId   must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCourse(Long categoryId, Long courseId);

    /**
     * Checks on availability of deck in folder with the given identifiers in DB.
     *
     * @param folderId must not be {@literal null}.
     * @param deckId   must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToDeckFromFolder(Long folderId, Long deckId);

    /**
     * Checks on availability of category with the given identifiers in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCourse(Long categoryId);

    /**
     * Checks on availability of deck that belong to course in category with the given identifiers in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @param courseId   must not be {@literal null}.
     * @param deckId     must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToDeck(Long categoryId, Long courseId, Long deckId);

    /**
     * Checks on availability of deck in category with the given identifiers in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @param deckId     must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToDeckFromCategory(Long categoryId, Long deckId);

    /**
     * Checks on availability of category with the given identifier in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToDeck(Long categoryId);

    /**
     * Checks on availability of card in deck with the given identifiers in DB.
     *
     * @param deckId must not be {@literal null}.
     * @param cardId must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCard(Long deckId, Long cardId);

    /**
     * Checks on availability of card that belong to deck in category with the given identifiers in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @param deckId     must not be {@literal null}.
     * @param cardId     true if it exist in DB
     * @return true if it exist in DB
     */
    boolean hasAccessToCard(Long categoryId, Long deckId, Long cardId);

    /**
     * Checks on availability of card that belong to deck in course that belong to category
     * with the given identifiers in DB.
     *
     * @param categoryId must not be {@literal null}.
     * @param courseId   must not be {@literal null}.
     * @param deckId     must not be {@literal null}.
     * @param cardId     must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCard(Long categoryId, Long courseId, Long deckId, Long cardId);

    /**
     * Checks on availability of folder with the given identifiers in DB.
     *
     * @param folderId must not be {@literal null}.
     * @return true if it exist in DB
     * @throws NotAuthorisedUserException if user is not authorised
     */
    boolean hasAccessToFolder(Long folderId) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for course.
     * It possible only for user that wrote it or for admin.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException if user is not authorised
     */
    boolean hasAccessToDeleteCommentForCourse(Long commentId) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for deck.
     * It possible only for user that wrote it or for admin.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException if user is not authorised
     */
    boolean hasAccessToDeleteCommentForDeck(Long commentId) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for deck.
     * It possible only for user that wrote it.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException if user is not authorised
     */
    boolean hasAccessToUpdateCommentForDeck(Long commentId) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for course.
     * It possible only for user that wrote it.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException if user is not authorised
     */
    boolean hasAccessToUpdateCommentForCourse(Long commentId) throws NotAuthorisedUserException;
}
