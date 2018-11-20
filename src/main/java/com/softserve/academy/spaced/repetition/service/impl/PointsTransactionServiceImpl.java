package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.userProfileDTO.SendPointsToFriendDTO;
import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.PointsTransactionRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.service.PointsTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PointsTransactionServiceImpl implements PointsTransactionService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private PointsTransactionRepository pointsTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public int checkPointsBalance(Long userId) {
        Integer expensesByUser = Optional.ofNullable(
                pointsTransactionRepository.getAllExpensesByUser(userId)).orElse(0);
        Integer incomeByUser = Optional.ofNullable(
                pointsTransactionRepository.getAllIncomeByUser(userId)).orElse(0);
        return incomeByUser - expensesByUser;
    }

    @Transactional
    @Override
    public SendPointsToFriendDTO sendPointsToFriend(SendPointsToFriendDTO sendPointsToFriendDTO) {
        User userTo = userRepository.findUserByAccountEmail(sendPointsToFriendDTO.getEmailTo());
        if (Objects.nonNull(userTo)) {
            User userFrom = userRepository.findUserByAccountEmail(sendPointsToFriendDTO.getEmailFrom());
            if (checkPointsBalance(userFrom.getId()) > sendPointsToFriendDTO.getPoints()) {
                PointsTransaction pointsTransaction = new PointsTransaction();
                pointsTransaction.setUserFrom(userFrom);
                pointsTransaction.setUserTo(userTo);
                pointsTransaction.setPoints((sendPointsToFriendDTO.getPoints()));
                pointsTransaction.setCreationDate(new Date());
                pointsTransaction.setReference(pointsTransaction);
                pointsTransactionRepository.save(pointsTransaction);
                sendPointsToFriendDTO.setPoints(checkPointsBalance(userFrom.getId()));
            } else {
                throw new IllegalArgumentException(messageSource.getMessage("message.validation.notEnoughPoints",
                        new Object[]{}, locale));
            }
        } else {
            throw new IllegalArgumentException(messageSource.getMessage("message.validation.emailNotExists",
                    new Object[]{}, locale));
        }
        return sendPointsToFriendDTO;
    }
}
