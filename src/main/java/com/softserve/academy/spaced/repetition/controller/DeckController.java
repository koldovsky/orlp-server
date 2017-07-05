package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DeckDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckDTOImpl;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.CardService;
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
    public List<DeckDTO> getAllDecksByCategoryId(@PathVariable Long id) {
        List<Deck> decks = deckService.getAllDecksByCategoryId(id);
        List<DeckDTOImpl> decksDTOImpl = new ArrayList<>();
        for (Deck deck : decks) {
            decksDTOImpl.add(new DeckDTOImpl(deck));
        }
        List<DeckDTO> deckDTO = new ArrayList<>();
        deckDTO.addAll(decksDTOImpl);

        return deckDTO;
    }

    @RequestMapping(value = "/decks/{id}", method = RequestMethod.GET)
    public DeckDTO getDeck(@PathVariable Long id) {
        DeckDTOImpl deckDTOImpl = new DeckDTOImpl(deckService.getDeck(id));
        return deckDTOImpl;
    }

    @RequestMapping(value = "/category/{category_id}/deck/{deck_id}/cards", method = RequestMethod.GET)
    public List<Card> getCardsByDeckId(@PathVariable Long deck_id) {
        return deckService.getAllCardsByDeckId(deck_id);
    }

    @RequestMapping(value = "/category/{category_id}/deck", method = RequestMethod.POST)
    public ResponseEntity<?> addCourse(@RequestBody Deck deck, @PathVariable Long category_id) {
        deck.setCategory(new Category(category_id));
        deckService.addDeck(deck);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/topRatedDecks", method = RequestMethod.GET)
    public List <DeckDTO> topRatedDecks() {
        List<Deck> decks = deckService.findTop4ByOrderById();
        List<DeckDTOImpl> decksDTOImpl = new ArrayList<>();
        for (Deck deck : decks) {
            decksDTOImpl.add(new DeckDTOImpl(deck));
        }
        List<DeckDTO> deckDTO = new ArrayList<>();
        deckDTO.addAll(decksDTOImpl);

        return deckDTO;
    }

//    @RequestMapping(value = "/decks/{id}", method = RequestMethod.PUT)
//    public void updateDeck(@PathVariable Long id, @RequestBody DeckDTO deck) {
//        deckService.updateDeck(id, deck);
//    }
//
//    @RequestMapping(value = "/decks/{id}", method = RequestMethod.DELETE)
//    public void deleteDeck(@PathVariable Long id) {
//        deckService.deleteDeck(id);
//    }
}
