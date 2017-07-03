package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.TopRatedDecksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TopRatedDecksController {
    @Autowired
    TopRatedDecksService topRatedDecksService;

    @RequestMapping(value = "/topRatedDecks", method = RequestMethod.GET)
    public List <Deck> topRatedDecks() {
        return topRatedDecksService.getTopRatedDecks();
    }
}
