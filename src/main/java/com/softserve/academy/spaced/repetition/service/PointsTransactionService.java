package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.TransactionException;

import java.util.List;

public interface PointsTransactionService {

    List<PointsTransaction> getTransactionsById (long userId);

    List<PointsTransaction> getAllTransactions ();

    void buyDeck(Long deckId) throws NotAuthorisedUserException, TransactionException;

    void buyCourse(Long courseId) throws NotAuthorisedUserException, TransactionException;
}
