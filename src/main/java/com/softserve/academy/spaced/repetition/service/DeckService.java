package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This interface processes all operations with decks.
 */
public interface DeckService {
    /**
     * Adds a new deck to the category
     *
     * @param deck       - the deck which will be add to the category.
     * @param categoryId - category id for which the deck will be added.
     */
    void addDeckToCategory(Deck deck, Long categoryId);

    /**
     * Adds a new deck to the course
     *
     * @param deck       - the deck which will be add to the category.
     * @param categoryId - id of the category for which belongs the course.
     * @param courseId   - course id for which will add the deck.
     */
    void addDeckToCourse(Deck deck, Long categoryId, Long courseId);

    /**
     * Updates the deck
     * @param updatedDeck - the new deck which will be set.
     * @param deckId      - id of the deck which will be updated.
     * @param categoryId  - category id for which the deck will be updated.
     */
    void updateDeck(Deck updatedDeck, Long deckId, Long categoryId);

    /**
     *
     * @param updatedDeck
     * @param deckId
     * @return
     */
    Deck updateDeckAdmin(Deck updatedDeck, Long deckId);

    /**
     * @param deckId
     */
    void deleteDeck(Long deckId);

    /**
     * @param newDeck
     * @param categoryId
     * @throws NotAuthorisedUserException
     */
    void createNewDeck(Deck newDeck, Long categoryId) throws NotAuthorisedUserException;

    /**
     * @param newDeck
     * @return
     * @throws NotAuthorisedUserException
     */
    Deck createNewDeckAdmin(Deck newDeck) throws NotAuthorisedUserException;

    /**
     * @param deckId
     * @throws NotAuthorisedUserException
     * @throws NotOwnerOperationException
     */
    void deleteOwnDeck(Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException;

    /**
     * @param updatedDeck
     * @param deckId
     * @param categoryId
     * @return
     * @throws NotAuthorisedUserException
     * @throws NotOwnerOperationException
     */
    Deck updateOwnDeck(Deck updatedDeck, Long deckId, Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException;

    /**
     * @return
     * @throws NotAuthorisedUserException
     */
    List<Deck> getAllDecksByUser() throws NotAuthorisedUserException;

    /**
     * @param deckId
     * @return
     * @throws NotAuthorisedUserException
     * @throws NotOwnerOperationException
     */
    Deck getDeckUser(Long deckId) throws NotAuthorisedUserException, NotOwnerOperationException;

    /**
     * @param categoryId
     * @param pageNumber
     * @param sortBy
     * @param ascending
     * @return
     */
    Page<Deck> getPageWithDecksByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending);

    /**
     * @param pageNumber
     * @param sortBy
     * @param ascending
     * @return
     */
    Page<Deck> getPageWithAllAdminDecks(int pageNumber, String sortBy, boolean ascending);

    /**
     * @param deckId
     * @return
     */
    String getSynthaxToHightlight(long deckId);

    /**
     * @param deckId
     * @return
     */
    Deck getDeck(Long deckId);

    /**
     * @param courseId
     * @return
     */
    List<Deck> getAllDecks(Long courseId);

    /**
     * @param categoryId
     * @return
     */
    List<Deck> getAllDecksByCategory(Long categoryId);

    /**
     * @return
     */
    List<Deck> getAllOrderedDecks();

    /**
     * @param deckId
     * @return
     */
    List<Card> getAllCardsByDeckId(Long deckId);
}
