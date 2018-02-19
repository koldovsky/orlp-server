package com.softserve.academy.spaced.repetition.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoListForCollection;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class DeckController {
    @Autowired
    private DeckService deckService;

    @Autowired
    private FolderService folderService;

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
    @ResponseStatus(HttpStatus.OK)
    public List<DeckPublicDTO> getAllDecksOrderByRating() {
        List<Deck> decksList = deckService.getAllOrderedDecks();
        return buildDtoListForCollection(decksList, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksOrderByRating()).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_COURSE)
    @GetMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks")
    @ResponseStatus(HttpStatus.OK)
    public List<DeckLinkByCourseDTO> getAllDecksByCourseId(@PathVariable Long categoryId, @PathVariable Long courseId) {
        List<Deck> decksList = deckService.getAllDecks(courseId);
        return buildDtoListForCollection(decksList, DeckLinkByCourseDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksByCourseId(categoryId, courseId)).withSelfRel());
    }

    @GetMapping(value = "/api/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public DeckPublicDTO getDeckById(@PathVariable Long deckId) {
        Deck deck = deckService.getDeck(deckId);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_CATEGORY)
    @GetMapping(value = "/api/categories/decks/{deckId}/cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CardPublicDTO> getCardsByCategoryAndDeck(@PathVariable Long deckId) {
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        return buildDtoListForCollection(cards, CardPublicDTO.class,
                linkTo(methodOn(DeckController.class).getCardsByCategoryAndDeck(deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_COURSE)
    @GetMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks/{deckId}/cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CardPublicDTO> getCardsByCourseAndDeck(@PathVariable Long categoryId, @PathVariable Long courseId,
                                                       @PathVariable Long deckId) {
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        return buildDtoListForCollection(cards, CardPublicDTO.class, linkTo(methodOn(DeckController.class)
                .getCardsByCourseAndDeck(categoryId, courseId, deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.CREATE_DECK_IN_CATEGORY)
    @PostMapping(value = "/api/categories/{category_id}/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckPublicDTO addDeckToCategory(@Validated(Request.class) @RequestBody Deck deck,
                                           @PathVariable Long category_id) {
        deckService.addDeckToCategory(deck, category_id);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.CREATE_DECK_IN_COURSE)
    @PostMapping(value = "/api/courses/{courseId}/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckPublicDTO addDeckToCourse(@Validated(Request.class) @RequestBody Deck deck, @PathVariable Long courseId) {
        deckService.addDeckToCourse(deck, courseId);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_ADMIN)
    @GetMapping(value = "/api/admin/decks")
    public ResponseEntity<Page<DeckOfUserManagedByAdminDTO>> getAllDecksForAdmin(@RequestParam(name = "p", defaultValue = "1") int pageNumber,
                                                                                 @RequestParam(name = "sortBy") String sortBy,
                                                                                 @RequestParam(name = "asc") boolean ascending) {
        Page<DeckOfUserManagedByAdminDTO> deckOfUserManagedByAdminDTO = deckService
                .getPageWithAllAdminDecks(pageNumber, sortBy, ascending).map((deck) -> {
                    Link selfLink = linkTo(methodOn(DeckController.class).getDeckById(deck.getId())).withSelfRel();
                    return buildDtoForEntity(deck, DeckOfUserManagedByAdminDTO.class, selfLink);
                });
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_ADMIN)
    @PostMapping(value = "/api/admin/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckOfUserManagedByAdminDTO addDeckForAdmin(@Validated(Request.class) @RequestBody Deck deck)
            throws NotAuthorisedUserException {
        Deck deckNew = deckService.createNewDeckAdmin(deck);
        folderService.addDeck(deckNew.getId());
        return buildDtoForEntity(deckNew, DeckOfUserManagedByAdminDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deckNew.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.EDIT_DECK_ADMIN)
    @PutMapping(value = "/api/admin/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public DeckOfUserManagedByAdminDTO updateDeckForAdmin(@Validated(Request.class) @RequestBody Deck deck,
                                                          @PathVariable Long deckId) {
        Deck updatedDeck = deckService.updateDeckAdmin(deck, deckId);
        return buildDtoForEntity(updatedDeck, DeckOfUserManagedByAdminDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(updatedDeck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.DELETE_DECK_ADMIN)
    @DeleteMapping(value = "/api/admin/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDeckForAdmin(@PathVariable Long deckId) throws NotAuthorisedUserException {
        folderService.deleteDeckFromAllUsers(deckId);
        deckService.deleteDeck(deckId);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_USER)
    @DeleteMapping(value = "/api/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public List<DeckPrivateDTO> deleteOwnDeckByUser(@PathVariable Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        deckService.deleteOwnDeck(deckId);
        List<Deck> decksList = deckService.getAllDecksByUser();
        return buildDtoListForCollection(decksList, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel());
    }

    @Auditable(action = AuditingAction.CREATE_DECK_USER)
    @PostMapping(value = "/api/categories/{categoryId}/decks")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckPrivateDTO addDeckForUser(@Validated(Request.class) @RequestBody Deck deck, @PathVariable Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        deckService.createNewDeck(deck, categoryId);
        folderService.addDeck(deck.getId());
        return buildDtoForEntity(deck, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getOneDeckForUser(deck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.EDIT_DECK_USER)
    @PutMapping(value = "/api/categories/{categoryId}/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public DeckPrivateDTO updateDeckForUser(@Validated(Request.class) @RequestBody Deck deck,
                                            @PathVariable Long deckId, @PathVariable Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck updatedDeck = deckService.updateOwnDeck(deck, deckId, categoryId);
        return buildDtoForEntity(updatedDeck, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getOneDeckForUser(updatedDeck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_USER)
    @GetMapping(value = "/api/users/folders/decks/own")
    @ResponseStatus(HttpStatus.OK)
    public List<DeckPrivateDTO> getAllDecksForUser() throws NotAuthorisedUserException {
        List<Deck> decksList = deckService.getAllDecksByUser();
        return buildDtoListForCollection(decksList, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_USER)
    @GetMapping(value = "/api/users/folders/decks/own/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    public DeckPrivateDTO getOneDeckForUser(@PathVariable Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck deck = deckService.getDeckUser(deckId);
        return buildDtoForEntity(deck, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getOneDeckForUser(deckId)).withSelfRel());
    }
}
