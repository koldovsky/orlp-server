package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.*;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class DeckController {
    @Autowired
    private DeckService deckService;

    @Autowired
    private FolderService folderService;

    //TODO preauthorize
    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_CATEGORY)
    @GetMapping(value = "/api/categories/{categoryId}/decks")
    public ResponseEntity<Page<DeckLinkByCategoryDTO>> getAllDecksByCategoryId(@PathVariable Long categoryId,
                                                                               @RequestParam(name = "p", defaultValue = "1")
                                                                                       int pageNumber,
                                                                               @RequestParam(name = "sortBy") String sortBy,
                                                                               @RequestParam(name = "asc") boolean ascending) {
        Page<DeckLinkByCategoryDTO> deckByCategoryDTOS = deckService
                .getPageWithDecksByCategory(categoryId, pageNumber, sortBy, ascending).map((deck) -> {
                    Link selfLink = linkTo(methodOn(DeckController.class)
                            .getAllDecksByCategoryId(categoryId, pageNumber, sortBy, ascending)).withRel("deck");
                    return buildDtoForEntity(deck, DeckLinkByCategoryDTO.class, selfLink);
                });
        return new ResponseEntity<>(deckByCategoryDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/ordered")
    public ResponseEntity<List<DeckPublicDTO>> getAllDecksOrderByRating() {
        List<Deck> decksList = deckService.getAllOrderedDecks();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksOrderByRating()).withRel("deck");
        List<DeckPublicDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckPublicDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    //TODO preauthorize
    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_COURSE)
    @GetMapping(value = "/api/courses/{courseId}/decks/{deckId}")
    public ResponseEntity<List<DeckLinkByCourseDTO>> getAllDecksByCourseId(@PathVariable Long courseId,
                                                                           @PathVariable Long deckId) {
        List<Deck> decksList = deckService.getAllDecks(courseId);
        Link collectionLink = linkTo(methodOn(DeckController.class)
                .getAllDecksByCourseId(courseId, deckId)).withRel("course");
        List<DeckLinkByCourseDTO> decks = DTOBuilder
                .buildDtoListForCollection(decksList, DeckLinkByCourseDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public DeckPublicDTO getDeckById(@PathVariable Long deckId) {
        Deck deck = deckService.getDeck(deckId);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class)
                        .getDeckById(deckId)).withSelfRel());
    }

    //TODO preauthorize
    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_CATEGORY)
    @GetMapping(value = "/api/categories/{categoryId}/decks/{deckId}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCategoryAndDeck(@PathVariable Long categoryId,
                                                                         @PathVariable Long deckId) {
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        Link collectionLink = linkTo(methodOn(DeckController.class)
                .getCardsByCategoryAndDeck(categoryId, deckId)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/{deckId}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByDeck(@PathVariable Long deckId) {
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByDeck(deckId)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    //TODO preauthorize
    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_COURSE)
    @GetMapping(value = "/api/categories/{categoryId}/courses/{courseId}/decks/{deckId}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCourseAndDeck(@PathVariable Long categoryId,
                                                                       @PathVariable Long courseId,
                                                                       @PathVariable Long deckId) {
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        Link collectionLink = linkTo(methodOn(DeckController.class)
                .getCardsByCourseAndDeck(categoryId, courseId, deckId)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    //TODO preauthorize
    @Auditable(action = AuditingAction.CREATE_DECK_IN_CATEGORY)
    @PostMapping(value = "/api/categories/{category_id}/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckPublicDTO addDeckToCategory(@Validated(Request.class) @RequestBody Deck deck,
                                                           @PathVariable Long category_id) {
        deckService.addDeckToCategory(deck, category_id);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deck.getId())).withSelfRel());
    }

    //TODO preauthorize
    @Auditable(action = AuditingAction.CREATE_DECK_IN_COURSE)
    @PostMapping(value = "/api/categories/{categoryId}/courses/{courseId}/decks")
    public ResponseEntity<DeckPublicDTO> addDeckToCourse(@Validated(Request.class) @RequestBody Deck deck,
                                                         @PathVariable Long categoryId,
                                                         @PathVariable Long courseId) {
        deckService.addDeckToCourse(deck, categoryId, courseId);
        Link selfLink = linkTo(methodOn(DeckController.class)
                .getDeckById(deck.getId())).withSelfRel();
        DeckPublicDTO deckPublicDTO = buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK)
    @PutMapping(value = "/api/user/{userId}/decks/{deckId}/{categoryId}")
    public void updateDeck(@Validated(Request.class) @RequestBody Deck deck,
                           @PathVariable Long deckId,
                           @PathVariable Long categoryId) {
        deckService.updateDeck(deck, deckId, categoryId);
    }

    @Auditable(action = AuditingAction.DELETE_DECK)
    @DeleteMapping(value = "/api/user/{userId}/decks/{deckId}")
    public void deleteDeck(@PathVariable Long deckId) {
        deckService.deleteDeck(deckId);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_ADMIN)
    @GetMapping(value = "/api/admin/decks")
    public ResponseEntity<Page<DeckOfUserManagedByAdminDTO>> getAllDecksForAdmin(@RequestParam(name = "p", defaultValue = "1")
                                                                                         int pageNumber,
                                                                                 @RequestParam(name = "sortBy") String sortBy,
                                                                                 @RequestParam(name = "asc") boolean ascending) {
        Page<DeckOfUserManagedByAdminDTO> deckOfUserManagedByAdminDTO = deckService
                .getPageWithAllAdminDecks(pageNumber, sortBy, ascending).map((deck) -> {
                    Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(deck.getId())).withSelfRel();
                    return buildDtoForEntity(deck, DeckOfUserManagedByAdminDTO.class, selfLink);
                });
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_ADMIN)
    @GetMapping(value = "/api/admin/decks/{deckId}")
    public ResponseEntity<DeckOfUserManagedByAdminDTO> getOneDeckForAdmin(@PathVariable Long deckId) {
        Deck deck = deckService.getDeck(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(deckId)).withSelfRel();
        DeckOfUserManagedByAdminDTO deckOfUserManagedByAdminDTO =
                buildDtoForEntity(deck, DeckOfUserManagedByAdminDTO.class, selfLink);
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_ADMIN)
    @PostMapping(value = "/api/admin/decks")
    public ResponseEntity<DeckOfUserManagedByAdminDTO> addDeckForAdmin(@Validated(Request.class) @RequestBody Deck deck)
            throws NotAuthorisedUserException {
        Deck deckNew = deckService.createNewDeckAdmin(deck);
        folderService.addDeck(deckNew.getId());
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(deckNew.getId())).withSelfRel();
        DeckOfUserManagedByAdminDTO deckOfUserManagedByAdminDTO =
                buildDtoForEntity(deckNew, DeckOfUserManagedByAdminDTO.class, selfLink);
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK_ADMIN)
    @PutMapping(value = "/api/admin/decks/{deckId}")
    public ResponseEntity updateDeckForAdmin(@Validated(Request.class) @RequestBody Deck deck,
                                             @PathVariable Long deckId) {
        Deck updatedDeck = deckService.updateDeckAdmin(deck, deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(updatedDeck.getId())).withSelfRel();
        DeckOfUserManagedByAdminDTO deckOfUserManagedByAdminDTO =
                buildDtoForEntity(updatedDeck, DeckOfUserManagedByAdminDTO.class, selfLink);
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_ADMIN)
    @DeleteMapping(value = "/api/admin/decks/{deckId}")
    public ResponseEntity deleteDeckForAdmin(@PathVariable Long deckId) throws NotAuthorisedUserException {
        folderService.deleteDeckFromAllUsers(deckId);
        deckService.deleteDeck(deckId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_USER)
    @DeleteMapping(value = "/api/private/deck/{deckId}")
    public ResponseEntity<List<DeckPrivateDTO>> deleteOwnDeckByUser(@PathVariable Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        deckService.deleteOwnDeck(deckId);
        List<Deck> decksList = deckService.getAllDecksByUser();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel();
        List<DeckPrivateDTO> decks = DTOBuilder
                .buildDtoListForCollection(decksList, DeckPrivateDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_USER)
    @PostMapping(value = "/api/private/categories/{categoryId}/decks")
    public ResponseEntity<DeckPrivateDTO> addDeckForUser(@Validated(Request.class) @RequestBody Deck deck,
                                                         @PathVariable Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        deckService.createNewDeck(deck, categoryId);
        folderService.addDeck(deck.getId());
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForUser(deck.getId())).withSelfRel();
        DeckPrivateDTO deckDTO = buildDtoForEntity(deck, DeckPrivateDTO.class, selfLink);
        return new ResponseEntity<>(deckDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK_USER)
    @PutMapping(value = "/api/private/categories/{categoryId}/deck/{deckId}")
    public ResponseEntity<DeckPrivateDTO> updateDeckForUser(@Validated(Request.class) @RequestBody Deck deck,
                                                            @PathVariable Long deckId,
                                                            @PathVariable Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck updatedDeck = deckService.updateOwnDeck(deck, deckId, categoryId);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForUser(updatedDeck.getId())).withSelfRel();
        DeckPrivateDTO deckDTO = buildDtoForEntity(updatedDeck, DeckPrivateDTO.class, selfLink);
        return new ResponseEntity<>(deckDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_USER)
    @GetMapping(value = "/api/private/user/folder/decks/own")
    public ResponseEntity<List<DeckPrivateDTO>> getAllDecksForUser() throws NotAuthorisedUserException {
        List<Deck> decksList = deckService.getAllDecksByUser();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel();
        List<DeckPrivateDTO> decks = DTOBuilder
                .buildDtoListForCollection(decksList, DeckPrivateDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_USER)
    @GetMapping(value = "/api/private/user/folder/decks/own/{deckId}")
    public ResponseEntity<DeckPrivateDTO> getOneDeckForUser(@PathVariable Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck deck = deckService.getDeckUser(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForUser(deckId)).withSelfRel();
        DeckPrivateDTO deckDTO = buildDtoForEntity(deck, DeckPrivateDTO.class, selfLink);
        return new ResponseEntity<>(deckDTO, HttpStatus.OK);
    }

    //TODO preauthorize
    @GetMapping("/api/private/user/folder/{folderId}/decks/{deckId}")
    public ResponseEntity<DeckLinkByFolderDTO> getDeckByFolderId(@PathVariable Long folderId, @PathVariable Long deckId) {
        Deck deck = deckService.getDeck(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByFolderId(folderId, deckId)).withSelfRel();
        DeckLinkByFolderDTO linkDTO = buildDtoForEntity(deck, DeckLinkByFolderDTO.class, selfLink);

        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    //TODO preauthorize
    @Auditable(action = AuditingAction.START_LEARNING_VIA_FOLDER)
    @GetMapping("/api/private/user/folder/{folderId}/decks/{deckId}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByFolderAndDeck(@PathVariable Long folderId,
                                                                       @PathVariable Long deckId) {
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByFolderAndDeck(folderId, deckId)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);

        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

}
