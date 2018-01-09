package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

/**
 * Create some kind of security for application.
 */
public interface AccessToUrlService {
    /**
     * Checks on availability of category with the given identifier in DB.
     *
     * @param category_id must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCategory(Long category_id);

    /**
     * Checks on availability of course in category with the given identifiers in DB.
     *
     * @param category_id must not be {@literal null}.
     * @param course_id   must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCourse(Long category_id, Long course_id);

    /**
     * Checks on availability of deck in folder with the given identifiers in DB.
     *
     * @param folder_id must not be {@literal null}.
     * @param deckId    must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToDeckFromFolder(Long folder_id, Long deckId);

    /**
     * Checks on availability of category with the given identifiers in DB.
     *
     * @param category_id must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCourse(Long category_id);

    /**
     * Checks on availability of deck that belong to course in category with the given identifiers in DB.
     *
     * @param category_id must not be {@literal null}.
     * @param course_id   must not be {@literal null}.
     * @param deck_id     must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToDeck(Long category_id, Long course_id, Long deck_id);

    /**
     * Checks on availability of deck in category with the given identifiers in DB.
     *
     * @param category_id must not be {@literal null}.
     * @param deck_id     must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToDeckFromCategory(Long category_id, Long deck_id);

    /**
     * Checks on availability of category with the given identifier in DB.
     *
     * @param category_id must not be {@literal null}.
     * @return
     */
    boolean hasAccessToDeck(Long category_id);

    /**
     * Checks on availability of card in deck with the given identifiers in DB.
     *
     * @param deck_id must not be {@literal null}.
     * @param card_id must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCard(Long deck_id, Long card_id);

    /**
     * Checks on availability of card that belong to deck in category with the given identifiers in DB.
     *
     * @param category_id must not be {@literal null}.
     * @param deck_id     must not be {@literal null}.
     * @param card_id     true if it exist in DB
     * @return true if it exist in DB
     */
    boolean hasAccessToCard(Long category_id, Long deck_id, Long card_id);

    /**
     * Checks on availability of card that belong to deck in course that belong to category
     * with the given identifiers in DB.
     *
     * @param category_id must not be {@literal null}.
     * @param course_id   must not be {@literal null}.
     * @param deck_id     must not be {@literal null}.
     * @param card_id     must not be {@literal null}.
     * @return true if it exist in DB
     */
    boolean hasAccessToCard(Long category_id, Long course_id, Long deck_id, Long card_id);

    /**
     * Checks on availability of folder with the given identifiers in DB.
     *
     * @param folder_id must not be {@literal null}.
     * @return true if it exist in DB
     * @throws NotAuthorisedUserException
     */
    boolean hasAccessToFolder(Long folder_id) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for course.
     * It possible only for user that wrote it or for admin.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException
     */
    boolean hasAccessToDeleteCommentForCourse(Long commentId) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for deck.
     * It possible only for user that wrote it or for admin.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException
     */
    boolean hasAccessToDeleteCommentForDeck(Long commentId) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for deck.
     * It possible only for user that wrote it.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException
     */
    boolean hasAccessToUpdateCommentForDeck(Long commentId) throws NotAuthorisedUserException;

    /**
     * Checks on possibility of deleting comment for course.
     * It possible only for user that wrote it.
     *
     * @param commentId must not be {@literal null}.
     * @return true if it is possible
     * @throws NotAuthorisedUserException
     */
    boolean hasAccessToUpdateCommentForCourse(Long commentId) throws NotAuthorisedUserException;
}
