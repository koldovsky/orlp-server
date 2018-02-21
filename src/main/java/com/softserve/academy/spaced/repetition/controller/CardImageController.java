package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.service.CardImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardImageController {

    @Autowired
    private CardImageService cardImageService;

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "api/cardImage/{id}")
    //TODO insert security
//    @PreAuthorize("hasPermission('CARD_IMAGE','DELETE')")
    public void deleteCardImage(@PathVariable long id) {
        cardImageService.deleteById(id);
    }

}
