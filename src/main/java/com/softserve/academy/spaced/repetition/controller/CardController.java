package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.service.AccessToUrlService;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards/{card_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#category_id, #course_id, #deck_id, #card_id)")
    public ResponseEntity<CardPublicDTO> getCardByCourseAndDeck(@PathVariable Long category_id, @PathVariable Long course_id, @PathVariable Long deck_id, @PathVariable Long card_id) {
           Card card = cardService.getCard(card_id);
           Link selfLink = linkTo(methodOn(CardController.class).getCardByCourseAndDeck(category_id, course_id, deck_id, card_id)).withSelfRel();
           CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
           return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards/{card_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#category_id, #deck_id, #card_id)")
    public ResponseEntity<CardPublicDTO> getCardByCategoryAndDeck(@PathVariable Long category_id, @PathVariable Long deck_id, @PathVariable Long card_id) {
        Card card = cardService.getCard(card_id);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCategoryAndDeck(category_id, deck_id, card_id)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards")
    public ResponseEntity<CardPublicDTO> addCardByCourseAndDeck(@RequestBody Card card, @PathVariable Long category_id, @PathVariable Long course_id, @PathVariable Long deck_id) {
        cardService.addCard(card, deck_id);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCourseAndDeck(category_id, course_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards")
    public ResponseEntity<CardPublicDTO> addCardByCategoryAndDeck(@RequestBody Card card, @PathVariable Long category_id, @PathVariable Long deck_id) {
        cardService.addCard(card, deck_id);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCategoryAndDeck(category_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards/{card_id}")
    public ResponseEntity<CardPublicDTO> updateCardByCourseAndDeck(@PathVariable Long category_id, @PathVariable Long course_id,
                                                                   @PathVariable Long deck_id, @PathVariable Long card_id, @RequestBody Card card) {
        cardService.updateCard(card_id, card);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCourseAndDeck(category_id, course_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards/{card_id}")
    public ResponseEntity<CardPublicDTO> updateCardByCategoryAndDeck(@PathVariable Long category_id, @PathVariable Long deck_id, @PathVariable Long card_id, @RequestBody Card card) {
        cardService.updateCard(card_id, card);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCategoryAndDeck(category_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/api/category/{categoryId}/decks/{deckId}/cards/{id}", "/api/courses/{courseId}/decks/{deckId}/cards/{id}"})
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
