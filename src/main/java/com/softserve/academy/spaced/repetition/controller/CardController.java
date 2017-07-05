package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.CardPublic;
import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @RequestMapping(value = {"/category/{categoryId}/decks/{deckId}/cards/{id}", "/courses/{courseId}/decks/{deckId}/cards/{id}"}, method = RequestMethod.GET)
    public CardPublic getCard(@PathVariable Long id) {
        CardPublicDTO card = new CardPublicDTO(cardService.getCard(id));
        return card;
    }

    @RequestMapping(value = {"/category/{categoryId}/decks/{deckId}/cards", "/courses/{courseId}/decks/{deckId}/cards"}, method = RequestMethod.POST)
    public ResponseEntity<?> addCard(@RequestBody Card card) {
        cardService.addCard(card);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = {"/category/{categoryId}/decks/{deckId}/cards/{id}", "/courses/{courseId}/decks/{deckId}/cards/{id}"}, method = RequestMethod.PUT)
    public void updateCard(@PathVariable Long id, @RequestBody Card card) {
        cardService.updateCard(id, card);
    }

    @RequestMapping(value = {"/category/{categoryId}/decks/{deckId}/cards/{id}", "/courses/{courseId}/decks/{deckId}/cards/{id}"}, method = RequestMethod.DELETE)
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
