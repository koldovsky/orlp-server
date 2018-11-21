package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.TransactionType;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.PointsTransactionService;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.PointsTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PointsTransactionServiceImpl implements PointsTransactionService {

    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    private PointsTransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DeckOwnershipRepository deckOwnershipRepository;

    @Autowired
    private CourseOwnershipRepository courseOwnershipRepository;

    @Autowired
    private MessageSource messageSource;

    @Transactional
    @Override
    public void buyDeck(Long deckId) throws NotAuthorisedUserException, PointsTransactionException {
        Deck deck = deckRepository.findOne(deckId);
        User userFrom = userService.getAuthorizedUser();
        Integer points = Optional.ofNullable(deck.getDeckPrice().getPrice()).orElse(0);
        if(userFrom.getPoints()>=points) {
            User userTo = userService.getUserById(deck.getCreatedBy());
            DeckOwnership deckOwnership = new DeckOwnership();
            deckOwnership.setDeckId(deckId);
            deckOwnership.setUserId(userFrom.getId());
            deckOwnership.setCreationDate(new Date());
            deckOwnership.setReference(deckOwnership);
            deckOwnershipRepository.save(deckOwnership);
            PointsTransaction transaction = new PointsTransaction(userFrom, userTo, points, TransactionType.DECK);
            transaction.setCreationDate(new Date());
            transaction.setReference(deckOwnership.getReference());
            transactionRepository.save(transaction);
            userService.updatePointsBalance(userFrom);
            userService.updatePointsBalance(userTo);
        } else {
            throw new PointsTransactionException(messageSource.getMessage("message.transaction.notEnoughPoints",
                    new Object[]{}, locale));
        }

    }

    @Transactional
    @Override
    public void buyCourse(Long courseId) throws NotAuthorisedUserException, PointsTransactionException {
        Course course = courseRepository.findOne(courseId);
        User userFrom = userService.getAuthorizedUser();
        Integer points = Optional.ofNullable(course.getCoursePrice().getPrice()).orElse(0);
        if(userFrom.getPoints()>=points) {
            User userTo = userService.getUserById(course.getCreatedBy());
            CourseOwnership courseOwnership = new CourseOwnership();
            courseOwnership.setCourseId(courseId);
            courseOwnership.setCreationDate(new Date());
            courseOwnership.setUserId(userFrom.getId());
            courseOwnership.setReference(courseOwnership);
            courseOwnershipRepository.save(courseOwnership);
            PointsTransaction transaction = new PointsTransaction(userFrom, userTo, points, TransactionType.COURSE);
            transaction.setCreationDate(new Date());
            transaction.setReference(courseOwnership.getReference());
            transactionRepository.save(transaction);
            userService.updatePointsBalance(userFrom);
            userService.updatePointsBalance(userTo);
        } else {
            throw new PointsTransactionException(messageSource.getMessage("message.transaction.notEnoughPoints",
                    new Object[]{}, locale));
        }

    }

    @Override
    public List<PointsTransaction> getTransactionsById (long userId) {
       return transactionRepository.findAllTransactionsByUserId(userId);
    }

    @Override
    public List<PointsTransaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

}
