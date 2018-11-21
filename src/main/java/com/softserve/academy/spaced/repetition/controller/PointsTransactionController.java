package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.PointsTransactionDTO;
import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import com.softserve.academy.spaced.repetition.service.PointsTransactionService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.PointsTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class PointsTransactionController {

    @Autowired
    private PointsTransactionService transactionService;

    @PostMapping(value = "/api/buy/deck/{deckId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity buyDeck(@PathVariable Long deckId)
            throws NotAuthorisedUserException, PointsTransactionException {
        transactionService.buyDeck(deckId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/buy/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity buyCourse(@PathVariable Long courseId) throws NotAuthorisedUserException, PointsTransactionException {
        transactionService.buyCourse(courseId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/api/private/user/{userId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PointsTransactionDTO>> getAllTransactionsById(@PathVariable Long userId) {
        List<PointsTransaction> list = transactionService.getTransactionsById(userId);
        Link collectionLink = linkTo(methodOn(PointsTransactionController.class).getAllTransactionsById(userId)).withSelfRel();
        List<PointsTransactionDTO> transactions = DTOBuilder.buildDtoListForCollection(list, PointsTransactionDTO.class, collectionLink);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/audit/transactions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PointsTransactionDTO>> getAllTransactions(){
        List<PointsTransaction> list = transactionService.getAllTransactions();
        Link collectionLink = linkTo(methodOn(PointsTransactionController.class).getAllTransactions()).withSelfRel();
        List<PointsTransactionDTO> transactions = DTOBuilder.buildDtoListForCollection(list, PointsTransactionDTO.class, collectionLink);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
