package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckCreateValidationDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckEditByAdminDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

/**
 * This interface proceeds all operations with decks.
 */
public interface DeckService {

    /**
     * Adds a new deck to the course
     *
     * @param deck     the deck which will be added to the category.
     * @param courseId course`s id to which the deck will be added.
     */
    void addDeckToCourse(Deck deck, Long courseId);

    /**
     * Updates the deck
     *
     * @param updatedDeck the new deck which will be set.
     * @param deckId      id of the deck which will be updated.
     * @param categoryId  category`s id for which the deck will be updated.
     */
    void updateDeck(Deck updatedDeck, Long deckId, Long categoryId);

    /**
     * Updates the deck by using the admin account
     *
     * @param updatedDeck the new deck which will be set.
     * @param deckId      id of the deck which will be updated.
     * @return the updated deck.
     */
    Deck updateDeckAdmin(DeckEditByAdminDTO updatedDeck, Long deckId);

    /**
     * Deletes the deck
     *
     * @param deckId id of the deck which will be deleted.
     */
    void deleteDeck(Long deckId);

    /**
     * Creates new deck
     *
     * @param newDeck    new deck which will be added.
     * @param categoryId category`s id for which the deck will be created.
     * @throws NotAuthorisedUserException if unauthorized user creates new deck.
     */
    void createNewDeck(Deck newDeck, Long categoryId) throws NotAuthorisedUserException;

    /**
     * Creates the deck by using the admin account.
     *
     * @param deckCreateValidationDTO new deck which will be added.
     * @return the created deck.
     * @throws NotAuthorisedUserException if unauthorized user creates new deck.
     */
    Deck createNewDeckAdmin(DeckCreateValidationDTO deckCreateValidationDTO) throws NotAuthorisedUserException;

    /**
     * Deletes the deck which was created by the user.
     *
     * @param deckId id of the deck which will be deleted.
     * @throws NotAuthorisedUserException if unauthorized user deletes the deck.
     * @throws NotOwnerOperationException if the user is not owner of the deck deletes the deck.
     */
    void deleteOwnDeck(Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException;

    /**
     * Updates the deck which was created by the user.
     *
     * @param updatedDeck the new deck which will be set.
     * @param deckId      id of the deck which will be updated.
     * @param categoryId  category`s id for which the deck will be updated.
     * @return updated deck.
     * @throws NotAuthorisedUserException if unauthorized user creates new deck.
     * @throws NotOwnerOperationException if the user is not owner of the deck updates the deck.
     */
    Deck updateOwnDeck(Deck updatedDeck, Long deckId, Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException;

    /**
     * Gives all of the decks witch was created by the user.
     *
     * @return list of the decks witch was created by the user.
     * @throws NotAuthorisedUserException if unauthorized user gets the decks.
     */
    List<Deck> getAllDecksByUser() throws NotAuthorisedUserException;

    /**
     * Gets the deck of the user.
     *
     * @param deckId deck`s id which will be got.
     * @return the deck of the user.
     * @throws NotAuthorisedUserException if unauthorized user gets the decks.
     * @throws NotOwnerOperationException if the user is not owner of the deck gets the deck.
     */
    Deck getDeckUser(Long deckId) throws NotAuthorisedUserException, NotOwnerOperationException;

    /**
     * Gives the page with decks which are sorted by category.
     *
     * @param categoryId category id for which the decks will be given.
     * @param pageNumber the pages number on which there is the category.
     * @param sortBy     the properties to sort by, must not be null or empty.
     * @param ascending  the value that determines how the elements must be sorted on the page.
     * @return the page with decks which are sorted by category.
     */
    Page<Deck> getPageWithDecksByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending);

    /**
     * Gives the page with decks which are created by admin.
     *
     * @param pageNumber the pages number on which there is the category.
     * @param sortBy     the properties to sort by, must not be null or empty.
     * @param ascending  the value that determines how the elements must be sorted on the page.
     * @return the page with decks which are created by admin.
     */
    Page<Deck> getPageWithAllAdminDecks(int pageNumber, String sortBy, boolean ascending);

    /**
     * Highlights syntax of the deck with the given identifier.
     *
     * @param deckId deck`s id for which syntax will highlight.
     * @return highlighted syntax.
     */
    String getSynthaxToHightlight(long deckId);

    /**
     * Gets the deck with the given identifier.
     *
     * @param deckId the deck which will searched.
     * @return the deck with the given identifier.
     */
    Deck getDeck(Long deckId);

    /**
     * Gets the list of the decks which belongs to course with given identifier.
     *
     * @param courseId course`s id for which the decks will be searched.
     * @return the list of the decks which belongs to course with given identifier.
     */
    List<Deck> getAllDecks(Long courseId);

    /**
     * Gets the list of the decks which belongs to category with given identifier.
     *
     * @param categoryId category`s id for which the decks will be searched.
     * @return the list of the decks which belongs to category with given identifier.
     */
    List<Deck> getAllDecksByCategory(Long categoryId);

    /**
     * Gets the list of decks that are ordered.
     *
     * @return the list of decks that are ordered.
     */
    List<Deck> getAllOrderedDecks();

    /**
     * Gets the list of cards that are in the deck by given identifier.
     *
     * @param deckId deck`s id from which the cards will be get.
     * @return list of cards that are in the deck by given identifier.
     */
    List<Card> getAllCardsByDeckId(Long deckId);

    Set<BigInteger> findDecksId(String searchString);

    Deck toggleDeckAccess(Long deckId);

    List<Deck> findAllDecksBySearch(String searchString);
}
