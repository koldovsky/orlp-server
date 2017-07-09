package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.CardPublic;
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

@RestController
@RequestMapping("/api")
public class DeckController {
    @Autowired
    private DeckService deckService;

    @RequestMapping(value = "/category/{category_id}/decks", method = RequestMethod.GET)
    public List<DeckPublicDTO> getAllDecksByCategoryId(@PathVariable Long category_id) {
        List<Deck> decks = deckService.getAllDecksByCategoryId(category_id);
        List<DeckPublicDTO> decksPublicDTO = new ArrayList<>();
        for (Deck deck : decks) {
            decksPublicDTO.add(new DeckPublicDTO(deck));
        }
        return decksPublicDTO;
    }

    @RequestMapping(value = "/category/{category_id}/decks/{deck_id}", method = RequestMethod.GET)
    public DeckPublicDTO getDeck(@PathVariable Long deck_id) {
        DeckPublicDTO deckPublicDTO = new DeckPublicDTO(deckService.getDeck(deck_id));
        return deckPublicDTO;
    }

    @RequestMapping(value = "/topDecks", method = RequestMethod.GET)
    public List <DeckPublicDTO> topRatedDecks() {
        List<Deck> decks = deckService.findTop4ByOrderById();
        List<DeckPublicDTO> decksPublic = new ArrayList<>();
        for (Deck deck : decks) {
            decksPublic.add(new DeckPublicDTO(deck));
        }
        return decksPublic;
    }

    @RequestMapping(value = "/category/{category_id}/decks/{deck_id}/cards", method = RequestMethod.GET)
    public List<CardPublic> getCardsByDeckId(@PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        List<CardPublic> cardsPublic = new ArrayList<>();
        for (Card card: cards) {
            cardsPublic.add(new CardPublicDTO(card));
        }
        return cardsPublic;
    }

    @RequestMapping(value = "/category/{category_id}/decks", method = RequestMethod.POST)
    public ResponseEntity<DeckPublicDTO> addDeck(@RequestBody Deck deck, @PathVariable Long category_id) {
        deckService.addDeck(deck, category_id);
        return new ResponseEntity<>(new DeckPublicDTO(deck), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{user_id}/decks/{deck_id}", method = RequestMethod.PUT)
    public void updateDeck(@RequestBody Deck deck, @PathVariable Long deck_id) {
        deckService.updateDeck(deck, deck_id);
    }

    @RequestMapping(value = "/user/{user_id}/decks/{deck_id}", method = RequestMethod.DELETE)
    public void deleteDeck(@PathVariable Long deck_id) {
        deckService.deleteDeck(deck_id);
    }
}
