package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class CardController {

    @Autowired
    private CardService cardService;

    @RequestMapping(value = {"/api/category/{category_id}/decks/{deck_id}/cards/{card_id}",
            "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards/{card_id}"}, method = RequestMethod.GET)
    public CardPublicDTO getCard(@PathVariable Long card_id) {
        Card card = cardService.getCard(card_id);
//        card.add(linkTo(DeckController.class).withSelfRel());
        Link selfLink = linkTo(methodOn(CardController.class).getCard(card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = new CardPublicDTO(card, selfLink);
        return cardPublicDTO;
    }

    @RequestMapping(value = {"/api/category/{categoryId}/decks/{deckId}/cards",
            "/api/courses/{courseId}/decks/{deckId}/cards"}, method = RequestMethod.POST)
    public ResponseEntity<CardPublicDTO> addCard(@RequestBody Card card, @PathVariable Long deckId) {
        cardService.addCard(card, deckId);
        Link selfLink = linkTo(methodOn(CardController.class).getCard(card.getId())).withSelfRel();
        return new ResponseEntity<>(new CardPublicDTO(card, selfLink), HttpStatus.OK);
    }

    @RequestMapping(value = {"/api/category/{categoryId}/decks/{deckId}/cards/{id}",
            "/api/courses/{courseId}/decks/{deckId}/cards/{id}"}, method = RequestMethod.PUT)
    public void updateCard(@PathVariable Long id, @RequestBody Card card) {
        cardService.updateCard(id, card);
    }

    @RequestMapping(value = {"/api/category/{categoryId}/decks/{deckId}/cards/{id}",
            "/api/courses/{courseId}/decks/{deckId}/cards/{id}"}, method = RequestMethod.DELETE)
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
