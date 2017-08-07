package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckLinkByFolderDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private DeckService deckService;

    @PutMapping("/api/user/folder/add/deck")
    public ResponseEntity <DeckPublicDTO> addDeckToFolder(@RequestBody Long deckId) {
        Deck deck = folderService.addDeck(deckId);
//        Link selfLink = linkTo(methodOn(DeckController.class).addDeckToFolder(id)).withSelfRel();
//        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);

        return null;
    }

    @GetMapping("/api/user/folder/{folder_id}/decks")
    public ResponseEntity <List <DeckPublicDTO>> getAllDecksWithFolder(@PathVariable Long folder_id) {
        List <Deck> deckList = folderService.getAllDecksByFolderId(folder_id);

        Link collectionLink = linkTo(methodOn(FolderController.class).getAllDecksWithFolder(folder_id)).withSelfRel();
        List <DeckPublicDTO> decks = DTOBuilder.buildDtoListForCollection(deckList, DeckPublicDTO.class, collectionLink);

        return new ResponseEntity <List <DeckPublicDTO>>(decks, HttpStatus.OK);
    }

    @GetMapping("/api/user/folder/{folder_id}/decks/{deck_id}")
    public ResponseEntity <DeckLinkByFolderDTO> getDeckByFolderId(@PathVariable Long folder_id, @PathVariable Long deck_id) {
        Deck deck = deckService.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(FolderController.class).getDeckByFolderId(folder_id, deck_id)).withSelfRel();
        DeckLinkByFolderDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByFolderDTO.class, selfLink);

        return new ResponseEntity <DeckLinkByFolderDTO>(linkDTO, HttpStatus.OK);
    }

    @GetMapping("/api/user/folder/{folder_id}/decks/{deck_id}/cards")
    public ResponseEntity <List <CardPublicDTO>> getCardsByFolderAndDeck(@PathVariable Long folder_id, @PathVariable Long deck_id) {
        List <Card> cards = deckService.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(FolderController.class).getCardsByFolderAndDeck(folder_id, deck_id)).withSelfRel();
        List <CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);

        return new ResponseEntity <List <CardPublicDTO>>(cardsPublic, HttpStatus.OK);
    }

}
