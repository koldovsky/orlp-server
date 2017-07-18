package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.*;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class DeckController {
    @Autowired
    private DeckService deckService;

    @GetMapping(value = "/api/category/{category_id}/decks")
    public ResponseEntity<List<DeckPublicDTO>> getAllDecksByCategoryId(@PathVariable Long category_id) {
        List<Deck> decksList = deckService.getAllDecks();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksByCategoryId(category_id)).withRel("course");
        List<DeckPublicDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckPublicDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks")
    public ResponseEntity<List<DeckPublicDTO>> getAllDecksByCourseId(@PathVariable Long category_id, @PathVariable Long course_id) {
        List<Deck> decksList = deckService.getAllDecks();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksByCourseId(category_id, course_id)).withRel("course");
        List<DeckPublicDTO> decks = DTOBuilder.buildDtoListForCollection(decksList, DeckPublicDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}")
    public ResponseEntity<DeckLinkByCategoryDTO> getDeckByCategoryId(@PathVariable Long category_id, @PathVariable Long deck_id) {
        Deck deck = deckService.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(category_id, deck_id)).withSelfRel();
        DeckLinkByCategoryDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByCategoryDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}")
    public ResponseEntity<DeckLinkByCourseDTO> getDeckByCourseId(@PathVariable Long category_id, @PathVariable Long course_id,
                                                                 @PathVariable Long deck_id) {
        Deck deck = deckService.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCourseId(category_id, course_id, deck_id)).withSelfRel();
        DeckLinkByCourseDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByCourseDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCategoryAndDeck(@PathVariable Long category_id, @PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByCategoryAndDeck(category_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCourseAndDeck(@PathVariable Long category_id, @PathVariable Long course_id, @PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByCourseAndDeck(category_id, course_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @PostMapping(value = "/api/category/{category_id}/decks")
    public ResponseEntity<DeckPublicDTO> addDeckToCategory(@RequestBody Deck deck, @PathVariable Long category_id) {
        deckService.addDeckToCategory(deck, category_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(category_id, deck.getId())).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckPublicDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/api/category/{category_id}/courses/{course_id}/decks")
    public ResponseEntity<DeckPublicDTO> addDeckToCourse(@RequestBody Deck deck, @PathVariable Long category_id, @PathVariable Long course_id) {
        deckService.addDeckToCourse(deck, category_id, course_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCourseId(category_id, course_id, deck.getId())).withSelfRel();
        return new ResponseEntity<>(new DeckPublicDTO(deck, selfLink), HttpStatus.OK);
    }

    @PutMapping(value = "/api/user/{user_id}/decks/{deck_id}")
    public void updateDeck(@RequestBody Deck deck, @PathVariable Long deck_id) {
        deckService.updateDeck(deck, deck_id);
    }

    @DeleteMapping(value = "/api/user/{user_id}/decks/{deck_id}")
    public void deleteDeck(@PathVariable Long deck_id) {
        deckService.deleteDeck(deck_id);
    }
}
