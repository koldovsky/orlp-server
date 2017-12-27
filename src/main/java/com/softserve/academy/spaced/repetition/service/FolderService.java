package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import java.util.List;

public interface FolderService {
    Deck addDeck(Long deckId) throws NotAuthorisedUserException;

    List<Deck> getAllDecksByFolderId(Long folderId);

    List<Long> getAllDecksIdWithFolder() throws NotAuthorisedUserException;

    void deleteDeck(Long deckId) throws NotAuthorisedUserException;

    void deleteDeckFromAllUsers(Long deckId) throws NotAuthorisedUserException;
}
