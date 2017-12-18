package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.dto.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.dto.impl.DeckLinkByFolderDTO;
import com.softserve.academy.spaced.repetition.dto.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.impl.DeckServiceImpl;
import com.softserve.academy.spaced.repetition.service.impl.FolderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class FolderController {

    @Autowired
    private FolderServiceImpl folderServiceImpl;

    @Autowired
    private DeckServiceImpl deckServiceImpl;

    @Auditable(action = AuditingAction.ADD_DECK_TO_FOLDER)
    @PutMapping("/api/user/folder/add/deck/{deckId}")
    public ResponseEntity<DeckPublicDTO> addDeckToFolder(@PathVariable Long deckId) throws NotAuthorisedUserException {
        Deck deck = folderServiceImpl.addDeck(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(deck.getCategory().getId(), deckId)).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);

        return new ResponseEntity<>(deckPublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_DECK_IN_FOLDER)
    @GetMapping("/api/private/user/folder/{folder_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToFolder(#folder_id)")
    public ResponseEntity<List<DeckLinkByFolderDTO>> getAllDecksWithFolder(@PathVariable Long folder_id) {
        List<Deck> deckList = folderServiceImpl.getAllDecksByFolderId(folder_id);

        Link collectionLink = linkTo(methodOn(FolderController.class).getAllDecksWithFolder(folder_id)).withSelfRel();
        List<DeckLinkByFolderDTO> decks = DTOBuilder.buildDtoListForCollection(deckList, DeckLinkByFolderDTO.class, collectionLink);

        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping("/api/private/user/folder/decks/id")
    public ResponseEntity<List<Long>> getIdAllDecksInFolder() throws NotAuthorisedUserException {
        List<Long> id = folderServiceImpl.getAllDecksIdWithFolder();

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/api/private/user/folder/{folder_id}/decks/{deck_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromFolder(#folder_id, #deck_id)")
    public ResponseEntity<DeckLinkByFolderDTO> getDeckByFolderId(@PathVariable Long folder_id, @PathVariable Long deck_id) {
        Deck deck = deckServiceImpl.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(FolderController.class).getDeckByFolderId(folder_id, deck_id)).withSelfRel();
        DeckLinkByFolderDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByFolderDTO.class, selfLink);

        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.START_LEARNING_VIA_FOLDER)
    @GetMapping("/api/private/user/folder/{folder_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromFolder(#folder_id, #deck_id)")
    public ResponseEntity<List<CardPublicDTO>> getCardsByFolderAndDeck(@PathVariable Long folder_id, @PathVariable Long deck_id) {
        List<Card> cards = deckServiceImpl.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(FolderController.class).getCardsByFolderAndDeck(folder_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);

        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK)
    @DeleteMapping(value = "/api/user/folder/decks/{deck_id}")
    public void deleteUserDeck(@PathVariable Long deck_id) throws NotAuthorisedUserException {
        folderServiceImpl.deleteDeck(deck_id);
    }
}
