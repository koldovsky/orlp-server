package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.CardImage;
import com.softserve.academy.spaced.repetition.service.CardImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardImageController {

    @Autowired
    private CardImageService cardImageService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/private/cardImage/{id}")
    public CardImage getCardImage(@PathVariable long id){
        return cardImageService.findOne(id);
    }

}
