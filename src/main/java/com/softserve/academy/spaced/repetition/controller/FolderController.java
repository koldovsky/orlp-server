package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckLinkByFolderDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoListForCollection;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Auditable(action = AuditingAction.ADD_DECK_TO_FOLDER)
    @PutMapping("/api/user/folder/add/deck/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public DeckPublicDTO addDeckToFolder(@PathVariable Long deckId) throws NotAuthorisedUserException {
        Deck deck = folderService.addDeck(deckId);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECK_IN_FOLDER)
    @GetMapping("/api/user/folder/{folderId}/decks")
    @ResponseStatus(HttpStatus.OK)
    public List<DeckLinkByFolderDTO> getAllDecksWithFolder(@PathVariable Long folderId) {
        List<Deck> deckList = folderService.getAllDecksByFolderId(folderId);
        return buildDtoListForCollection(deckList, DeckLinkByFolderDTO.class,
                linkTo(methodOn(FolderController.class).getAllDecksWithFolder(folderId)).withSelfRel());
    }

    @GetMapping("/api/user/folder/decksId")
    public ResponseEntity<List<Long>> getIdAllDecksInFolder() throws NotAuthorisedUserException {
        List<Long> id = folderService.getAllDecksIdWithFolder();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK)
    @DeleteMapping(value = "/api/user/folder/decks/{deckId}")
    public void deleteUserDeck(@PathVariable Long deckId) throws NotAuthorisedUserException {
        folderService.deleteDeck(deckId);
    }
}
