package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import java.util.List;

public interface FolderService {
    Deck addDeck(Long deckId) throws NotAuthorisedUserException;

    List<Deck> getAllDecksByFolderId(Long folder_id);

    List<Long> getAllDecksIdWithFolder() throws NotAuthorisedUserException;

    void deleteDeck(Long deck_id) throws NotAuthorisedUserException;

    void deleteDeckFromAllUsers(Long deck_id) throws NotAuthorisedUserException;
}
