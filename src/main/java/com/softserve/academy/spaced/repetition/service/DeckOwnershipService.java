package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;

public interface DeckOwnershipService {

    boolean checkIsBoughtDeck (Long deckId) throws NotAuthorisedUserException;
}
