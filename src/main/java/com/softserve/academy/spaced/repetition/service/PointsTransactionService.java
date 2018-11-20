package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.TransactionDTO;
import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.TransactionException;

import java.util.List;

public interface PointsTransactionService {
    PointsTransaction givePoints(long userId, int points);

    PointsTransaction makeTransaction(TransactionDTO transaction) throws NotAuthorisedUserException, TransactionException;

    List<PointsTransaction> getTransactionsById (long userId);

    List<PointsTransaction> getAllTransactions ();

}
