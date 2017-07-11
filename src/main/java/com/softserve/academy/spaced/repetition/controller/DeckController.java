package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@CrossOrigin
public class DeckController {
    @Autowired
    private DeckService deckService;

    @RequestMapping(value = {"/api/category/{category_id}/decks",
            "/api/category/{category_id}/courses/{course_id}/decks"}, method = RequestMethod.GET)
    public List<DeckPublicDTO> getAllDecksByCategoryId(@PathVariable Long category_id) {
        List<Deck> decks = deckService.getAllDecksByCategoryId(category_id);
        List<DeckPublicDTO> decksPublicDTO = new ArrayList<>();
        for (Deck deck : decks) {
            deck.add(linkTo(DeckController.class).slash(deck.getId()).withSelfRel());
            decksPublicDTO.add(new DeckPublicDTO(deck));
        }
        return decksPublicDTO;
    }

    @RequestMapping(value = {"/api/category/{category_id}/decks/{deck_id}",
            "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}"}, method = RequestMethod.GET)
    public DeckPublicDTO getDeck(@PathVariable Long deck_id) {
        Deck deck = deckService.getDeck(deck_id);
        deck.add(linkTo(DeckController.class).withSelfRel());
        DeckPublicDTO deckPublicDTO = new DeckPublicDTO(deck);
        return deckPublicDTO;
    }

    @RequestMapping(value = "/api/topDecks", method = RequestMethod.GET)
    public List <DeckPublicDTO> topRatedDecks() {
        List<Deck> decks = deckService.findTop4ByOrderById();
        List<DeckPublicDTO> decksPublic = new ArrayList<>();
        for (Deck deck : decks) {
            deck.add(linkTo(DeckController.class).slash(deck.getId()).withSelfRel());
            decksPublic.add(new DeckPublicDTO(deck));
        }
        return decksPublic;
    }

    @RequestMapping(value = {"/api/category/{category_id}/decks/{deck_id}/cards",
            "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards"}, method = RequestMethod.GET)
    public List<CardPublicDTO> getCardsByDeckId(@PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        List<CardPublicDTO> cardsPublic = new ArrayList<>();
        for (Card card: cards) {
            card.add(linkTo(DeckController.class).slash(card.getId()).withSelfRel());
            cardsPublic.add(new CardPublicDTO(card));
        }
        return cardsPublic;
    }

    @RequestMapping(value = "/api/category/{category_id}/decks", method = RequestMethod.POST)
    public ResponseEntity<DeckPublicDTO> addDeck(@RequestBody Deck deck, @PathVariable Long category_id) {
        deckService.addDeck(deck, category_id);
        return new ResponseEntity<>(new DeckPublicDTO(deck), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user/{user_id}/decks/{deck_id}", method = RequestMethod.PUT)
    public void updateDeck(@RequestBody Deck deck, @PathVariable Long deck_id) {
        deckService.updateDeck(deck, deck_id);
    }

    @RequestMapping(value = "/api/user/{user_id}/decks/{deck_id}", method = RequestMethod.DELETE)
    public void deleteDeck(@PathVariable Long deck_id) {
        deckService.deleteDeck(deck_id);
    }
}
