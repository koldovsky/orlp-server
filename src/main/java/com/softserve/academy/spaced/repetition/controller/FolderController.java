package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.DeckLinkByFolderDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
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
    private FolderService folderService;

    @Autowired
    private DeckService deckService;

    @Auditable(action = AuditingAction.ADD_DECK_TO_FOLDER)
    @PutMapping("/api/user/folder/add/deck/{deckId}")
    public ResponseEntity<DeckPublicDTO> addDeckToFolder(@PathVariable Long deckId) throws NotAuthorisedUserException {
        Deck deck = folderService.addDeck(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class)
                .getDeckByCategoryId(deck.getCategory().getId(), deckId)).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);

        return new ResponseEntity<>(deckPublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_DECK_IN_FOLDER)
    @GetMapping("/api/private/user/folder/{folderId}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToFolder(#folderId)")
    public ResponseEntity<List<DeckLinkByFolderDTO>> getAllDecksWithFolder(@PathVariable Long folderId) {
        List<Deck> deckList = folderService.getAllDecksByFolderId(folderId);

        Link collectionLink = linkTo(methodOn(FolderController.class).getAllDecksWithFolder(folderId)).withSelfRel();
        List<DeckLinkByFolderDTO> decks = DTOBuilder
                .buildDtoListForCollection(deckList, DeckLinkByFolderDTO.class, collectionLink);

        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping("/api/private/user/folder/decks/id")
    public ResponseEntity<List<Long>> getIdAllDecksInFolder() throws NotAuthorisedUserException {
        List<Long> id = folderService.getAllDecksIdWithFolder();

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/api/private/user/folder/{folderId}/decks/{deckId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromFolder(#folderId, #deckId)")
    public ResponseEntity<DeckLinkByFolderDTO> getDeckByFolderId(@PathVariable Long folderId, @PathVariable Long deckId) {
        Deck deck = deckService.getDeck(deckId);
        Link selfLink = linkTo(methodOn(FolderController.class).getDeckByFolderId(folderId, deckId)).withSelfRel();
        DeckLinkByFolderDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByFolderDTO.class, selfLink);

        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.START_LEARNING_VIA_FOLDER)
    @GetMapping("/api/private/user/folder/{folderId}/decks/{deckId}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromFolder(#folderId, #deckId)")
    public ResponseEntity<List<CardPublicDTO>> getCardsByFolderAndDeck(@PathVariable Long folderId,
                                                                       @PathVariable Long deckId) {
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        Link collectionLink = linkTo(methodOn(FolderController.class)
                .getCardsByFolderAndDeck(folderId, deckId)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);

        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK)
    @DeleteMapping(value = "/api/user/folder/decks/{deckId}")
    public void deleteUserDeck(@PathVariable Long deckId) throws NotAuthorisedUserException {
        folderService.deleteDeck(deckId);
    }
}
