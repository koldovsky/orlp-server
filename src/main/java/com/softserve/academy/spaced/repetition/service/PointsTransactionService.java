package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckBuyDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpledto.userprofiledto.SendPointsToFriendDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.PointsTransactionException;

import java.util.List;

public interface PointsTransactionService {

    List<PointsTransaction> getTransactionsById(long userId);

    List<PointsTransaction> getAllTransactions();

    DeckBuyDTO buyDeck(Long deckId) throws NotAuthorisedUserException, PointsTransactionException;

    void buyCourse(Long courseId) throws NotAuthorisedUserException, PointsTransactionException;

    SendPointsToFriendDTO sendPointsToFriend(SendPointsToFriendDTO sendPointsToFriendDTO) throws NotAuthorisedUserException;
}
