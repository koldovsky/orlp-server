package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.CardPublic;
import com.softserve.academy.spaced.repetition.DTO.DeckPublic;
import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DeckController {
    @Autowired
    private DeckService deckService;

    @RequestMapping(value = "/category/{id}/decks", method = RequestMethod.GET)
    public List<DeckPublic> getAllDecksByCategoryId(@PathVariable Long id) {
        List<Deck> decks = deckService.getAllDecksByCategoryId(id);
        List<DeckPublic> decksPublic = new ArrayList<>();
        for (Deck deck : decks) {
            decksPublic.add(new DeckPublicDTO(deck));
        }
        return decksPublic;
    }

    @RequestMapping(value = "/decks/{id}", method = RequestMethod.GET)
    public DeckPublic getDeck(@PathVariable Long id) {
        DeckPublic deckPublic = new DeckPublicDTO(deckService.getDeck(id));
        return deckPublic;
    }

    @RequestMapping(value = "/topRatedDecks", method = RequestMethod.GET)
    public List <DeckPublic> topRatedDecks() {
        List<Deck> decks = deckService.findTop4ByOrderById();
        List<DeckPublic> decksPublic = new ArrayList<>();
        for (Deck deck : decks) {
            decksPublic.add(new DeckPublicDTO(deck));
        }
        return decksPublic;
    }

    @RequestMapping(value = "/category/{category_id}/deck/{deck_id}/cards", method = RequestMethod.GET)
    public List<CardPublic> getCardsByDeckId(@PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        List<CardPublic> cardsPublic = new ArrayList<>();
        for (Card card: cards) {
            cardsPublic.add(new CardPublicDTO(card));
        }
        return cardsPublic;
    }

    @RequestMapping(value = "/category/{category_id}/deck", method = RequestMethod.POST)
    public ResponseEntity<?> addCourse(@RequestBody Deck deck, @PathVariable Long category_id) {
        deck.setCategory(new Category(category_id));
        deckService.addDeck(deck);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

//    @RequestMapping(value = "/decks/{id}", method = RequestMethod.PUT)
//    public void updateDeck(@PathVariable Long id, @RequestBody DeckPublic deck) {
//        deckService.updateDeck(id, deck);
//    }
//
//    @RequestMapping(value = "/decks/{id}", method = RequestMethod.DELETE)
//    public void deleteDeck(@PathVariable Long id) {
//        deckService.deleteDeck(id);
//    }
}
