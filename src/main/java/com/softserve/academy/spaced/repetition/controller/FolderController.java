package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckLinkByFolderDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api/user/folder")
public class FolderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private FolderService folderService;

    @Auditable(action = AuditingAction.ADD_DECK_TO_FOLDER)
    @PutMapping("/add/deck/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('FOLDER','UPDATE')")
    public DeckPublicDTO addDeckToFolder(@PathVariable Long deckId) throws NotAuthorisedUserException {
        LOGGER.debug("Adding deck with id: {} to folder", deckId);
        Deck deck = folderService.addDeck(deckId);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECK_IN_FOLDER)
    @GetMapping("/{folderId}/decks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('FOLDER','READ')")
    public List<DeckLinkByFolderDTO> getAllDecksWithFolder(@PathVariable Long folderId) {
        List<Deck> deckList = folderService.getAllDecksByFolderId(folderId);
        return buildDtoListForCollection(deckList, DeckLinkByFolderDTO.class,
                linkTo(methodOn(FolderController.class).getAllDecksWithFolder(folderId)).withSelfRel());
    }

    @GetMapping("/decksId")
    @PreAuthorize("hasPermission('FOLDER','READ')")
    public ResponseEntity<List<Long>> getIdAllDecksInFolder() throws NotAuthorisedUserException {
        List<Long> id = folderService.getAllDecksIdWithFolder();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK)
    @DeleteMapping(value = "/decks/{deckId}")
    @PreAuthorize("hasPermission('FOLDER','DELETE')")
    public void deleteUserDeck(@PathVariable Long deckId) throws NotAuthorisedUserException {
        LOGGER.debug("Deleting deck with id: {}", deckId);
        folderService.deleteDeck(deckId);
    }
}
