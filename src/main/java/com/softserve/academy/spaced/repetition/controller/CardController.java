package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by askol on 6/30/2017.
 */
@RestController
@RequestMapping("/api")
public class CardController {
    CardService cardService;
    @RequestMapping(value = "/cards", method = RequestMethod.GET)
    public Collection<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @RequestMapping(value = "/cards/{id}", method = RequestMethod.GET)
    public Card getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @RequestMapping(value = "/cards", method = RequestMethod.POST)
    public ResponseEntity<?> addCard(@RequestBody Card card) {
        cardService.addCard(card);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/cards/{id}", method = RequestMethod.PUT)
    public void updateCard(@PathVariable Long id, @RequestBody Card card) {
        cardService.updateCard(id, card);
    }

    @RequestMapping(value = "/cards/{id}", method = RequestMethod.DELETE)
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
