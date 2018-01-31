package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

import java.util.List;

/**
 * This interface proceeds all operations with folders which are keeping the decks.
 */
public interface FolderService {
    /**
     * Adds a new deck.
     *
     * @param deckId deck`s id.
     * @return added deck.
     * @throws NotAuthorisedUserException if unauthorized user adds new deck data.
     */
    Deck addDeck(Long deckId) throws NotAuthorisedUserException;

    /**
     * Gets the list af deck`s which are in the folder.
     *
     * @param folderId identifier of the folder in which will be searched the decks.
     * @return list af deck`s which are in the folder.
     */

    List<Deck> getAllDecksByFolderId(Long folderId);

    /**
     * Gets the list of deck`s id which are in the folder.
     *
     * @return the list of deck`s id which are in the folder.
     * @throws NotAuthorisedUserException if unauthorized user used this operation.
     */

    List<Long> getAllDecksIdWithFolder() throws NotAuthorisedUserException;

    /**
     * Deletes the deck from folder by identifier.
     *
     * @param deckId the deck identifier which will be deleted.
     * @throws NotAuthorisedUserException if unauthorized user used this operation.
     */
    void deleteDeck(Long deckId) throws NotAuthorisedUserException;

    /**
     * Deletes the deck from all folders of users.
     *
     * @param deckId the deck identifier which will be deleted.
     * @throws NotAuthorisedUserException if unauthorized user used this operation.
     */
    void deleteDeckFromAllUsers(Long deckId) throws NotAuthorisedUserException;
}
