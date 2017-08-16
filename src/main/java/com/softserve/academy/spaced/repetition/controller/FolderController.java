package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckLinkByCategoryDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckLinkByFolderDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingActionType;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.service.UserService;
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
    private UserService userService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private DeckService deckService;

    @Auditable(actionType = AuditingActionType.ADD_DECK_TO_FOLDER)
    @PutMapping("/api/user/folder/add/deck/{deckId}")
    public ResponseEntity<DeckPublicDTO> addDeckToFolder(@PathVariable Long deckId) {
        Deck deck = folderService.addDeck(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(deck.getCategory().getId(), deckId)).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);

        return new ResponseEntity<DeckPublicDTO>(deckPublicDTO, HttpStatus.OK);
    }

    @Auditable(actionType = AuditingActionType.VIEW_DECK_IN_FOLDER)
    @GetMapping("/api/private/user/folder/{folder_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToFolder(#folder_id)")
    public ResponseEntity<List<DeckLinkByFolderDTO>> getAllDecksWithFolder(@PathVariable Long folder_id) {
        List<Deck> deckList = folderService.getAllDecksByFolderId(folder_id);

        Link collectionLink = linkTo(methodOn(FolderController.class).getAllDecksWithFolder(folder_id)).withSelfRel();
        List<DeckLinkByFolderDTO> decks = DTOBuilder.buildDtoListForCollection(deckList, DeckLinkByFolderDTO.class, collectionLink);

        return new ResponseEntity<List<DeckLinkByFolderDTO>>(decks, HttpStatus.OK);
    }

    @GetMapping("/api/private/user/folder/decks/id")
    public ResponseEntity<List<Long>> getIdAllDecksInFolder() {
        List<Long> id = folderService.getAllDecksIdWithFolder();

        return new ResponseEntity<List<Long>>(id, HttpStatus.OK);
    }

    @GetMapping("/api/private/user/folder/{folder_id}/decks/{deck_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromFolder(#folder_id, #deck_id)")
    public ResponseEntity<DeckLinkByFolderDTO> getDeckByFolderId(@PathVariable Long folder_id, @PathVariable Long deck_id) {
        Deck deck = deckService.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(FolderController.class).getDeckByFolderId(folder_id, deck_id)).withSelfRel();
        DeckLinkByFolderDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByFolderDTO.class, selfLink);

        return new ResponseEntity<DeckLinkByFolderDTO>(linkDTO, HttpStatus.OK);
    }

    @Auditable(actionType = AuditingActionType.START_LEARNING_VIA_FOLDER)
    @GetMapping("/api/private/user/folder/{folder_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromFolder(#folder_id, #deck_id)")
    public ResponseEntity<List<CardPublicDTO>> getCardsByFolderAndDeck(@PathVariable Long folder_id, @PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(FolderController.class).getCardsByFolderAndDeck(folder_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);

        return new ResponseEntity<List<CardPublicDTO>>(cardsPublic, HttpStatus.OK);
    }
}
