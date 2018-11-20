package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.TransactionDTO;
import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.PointsTransactionRepository;
import com.softserve.academy.spaced.repetition.service.PointsTransactionService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class PointsTransactionServiceImpl implements PointsTransactionService {

    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    private PointsTransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Transactional
    @Override
    public PointsTransaction makeTransaction(TransactionDTO transaction) throws NotAuthorisedUserException, TransactionException {
        User userFrom = userService.getAuthorizedUser();
        int points = transaction.getPoints();
        User userTo = userService.getUserById(transaction.getUserToId());
        PointsTransaction newTransaction = new PointsTransaction(userFrom, userTo, points);
        newTransaction.setCreationDate(transaction.getCreationDate());
        newTransaction.setReference(newTransaction);
        if(userFrom.getPoints()>= points) {
            transactionRepository.save(newTransaction);
            userService.updatePointsBalance(userFrom);
            userService.updatePointsBalance(userTo);
        } else {
            throw new TransactionException(messageSource.getMessage("message.transaction.notEnoughPoints",
                    new Object[]{}, locale));
        }

        return new PointsTransaction();
    }

    @Override
    public PointsTransaction givePoints(long user_id, int points) {

        return new PointsTransaction();
    }

    @Override
    public List<PointsTransaction> getTransactionsById (long userId) {
       return transactionRepository.findAllByUserId(userId);
    }

    @Override
    public List<PointsTransaction> getAllTransactions(){
        return transactionRepository.getAllTransactions();
    }

}
