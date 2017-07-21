package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.service.DeckRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class DeckRatingController{

    @Autowired
    private DeckRatingService deckRatingService;

    @PostMapping("api/rating")
    public DeckRating addDeckRating(@RequestBody DeckRating deckRating){
        return deckRatingService.addDeckRating(deckRating);
    }
}
