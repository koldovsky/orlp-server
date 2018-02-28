package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.service.CardImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardImageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardImageService cardImageService;

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "api/cardImage/{id}")
    @PreAuthorize("hasPermission('CARD_IMAGE','DELETE')")
    public void deleteCardImage(@PathVariable long id) {
        LOGGER.debug("Deleting image of card by id: {}", id);
        cardImageService.deleteById(id);
    }

}
