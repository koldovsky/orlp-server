package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import org.springframework.data.domain.Page;
import java.util.List;

public interface DeckService {
    void addDeckToCategory(Deck deck, Long categoryId);

    void addDeckToCourse(Deck deck, Long categoryId, Long courseId);

    void updateDeck(Deck updatedDeck, Long deckId, Long categoryId);

    Deck updateDeckAdmin(Deck updatedDeck, Long deckId);

    void deleteDeck(Long deckId);

    void createNewDeck(Deck newDeck, Long categoryId) throws NotAuthorisedUserException;

    Deck createNewDeckAdmin(Deck newDeck) throws NotAuthorisedUserException;

    void deleteOwnDeck(Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException;

    Deck updateOwnDeck(Deck updatedDeck, Long deckId, Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException;

    List<Deck> getAllDecksByUser() throws NotAuthorisedUserException;

    Deck getDeckUser(Long deckId) throws NotAuthorisedUserException, NotOwnerOperationException;

    Page<Deck> getPageWithDecksByCategory(long categoryId, int pageNumber, String sortBy, boolean ascending);

    Page<Deck> getPageWithAllAdminDecks(int pageNumber, String sortBy, boolean ascending);

    String getSynthaxToHightlight(long deckId);
}
