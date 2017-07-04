package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeckController {
    @Autowired
    private DeckService deckService;

    @RequestMapping(value = "/category/decks/{id}", method = RequestMethod.GET)
    public List<Deck> getAllDecksByCategoryId(@PathVariable Long id) {
        return deckService.getAllDecksByCategoryId(id);
    }

    @RequestMapping(value = "/decks/{id}", method = RequestMethod.GET)
    public Deck getDeck(@PathVariable Long id) {
        return deckService.getDeck(id);
    }

    @RequestMapping(value = "/decks", method = RequestMethod.POST)
    public ResponseEntity<?> addCourse(@RequestBody Deck deck) {
        deckService.addDeck(deck);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/decks/{id}", method = RequestMethod.PUT)
    public void updateDeck(@PathVariable Long id, @RequestBody Deck deck) {
        deckService.updateDeck(id, deck);
    }

    @RequestMapping(value = "/decks/{id}", method = RequestMethod.DELETE)
    public void deleteDeck(@PathVariable Long id) {
        deckService.deleteDeck(id);
    }
}
