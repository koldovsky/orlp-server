package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.*;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.DeckService;
import com.softserve.academy.spaced.repetition.service.FolderService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotOwnerOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoListForCollection;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class DeckController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeckCommentController.class);

    @Autowired
    private DeckService deckService;

    @Autowired
    private FolderService folderService;

    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_CATEGORY)

    @GetMapping(value = "/api/categories/{categoryId}/decks")
    @PreAuthorize("hasPermission('DECK','READ')")
    public ResponseEntity<Page<DeckLinkByCategoryDTO>> getAllDecksByCategoryId(@PathVariable Long categoryId,
                                                                               @RequestParam(name = "p", defaultValue = "1")
                                                                                       int pageNumber,
                                                                               @RequestParam(name = "sortBy") String sortBy,
                                                                               @RequestParam(name = "asc") boolean ascending) {
        LOGGER.debug("View all decks by category with id {}", categoryId);
        Page<DeckLinkByCategoryDTO> deckByCategoryDTOS = deckService
                .getPageWithDecksByCategory(categoryId, pageNumber, sortBy, ascending).map(deck -> {
                    Link selfLink = linkTo(methodOn(DeckController.class)
                            .getAllDecksByCategoryId(categoryId, pageNumber, sortBy, ascending)).withRel("deck");
                    return buildDtoForEntity(deck, DeckLinkByCategoryDTO.class, selfLink);
                });
        return new ResponseEntity<>(deckByCategoryDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/ordered")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','READ')")
    public List<DeckPublicDTO> getAllDecksOrderByRating() {
        LOGGER.debug("View all decks ordered by rating");
        List<Deck> decksList = deckService.getAllOrderedDecks();
        return buildDtoListForCollection(decksList, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksOrderByRating()).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_COURSE)
    @GetMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','READ')")
    public List<DeckLinkByCourseDTO> getAllDecksByCourseId(@PathVariable Long categoryId, @PathVariable Long courseId) {
        LOGGER.debug("View all decks by course with id {}", courseId);
        List<Deck> decksList = deckService.getAllDecks(courseId);
        return buildDtoListForCollection(decksList, DeckLinkByCourseDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksByCourseId(categoryId, courseId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.EDIT_DECK)
    @PutMapping("/api/cabinet/decks/{deckId}/toggle/access")
    @PreAuthorize("hasPermission('DECK','UPDATE') && @deckServiceImpl.getDeck(#deckId).createdBy==principal.id")
    public DeckLinkByCategoryDTO updateDeckAccess(@PathVariable Long deckId) {
        LOGGER.debug("Toggle deck with id: {}", deckId);
        Deck deck = deckService.toggleDeckAccess(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckById(deck.getId())).withSelfRel();
        return buildDtoForEntity(deck, DeckLinkByCategoryDTO.class, selfLink);
    }

    @GetMapping(value = "/api/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','READ')")
    public DeckPublicDTO getDeckById(@PathVariable Long deckId) {
        LOGGER.debug("View deck by id {}", deckId);
        Deck deck = deckService.getDeck(deckId);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_CATEGORY)
    @GetMapping(value = "/api/categories/decks/{deckId}/cards")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','READ')")
    public List<CardPublicDTO> getCardsByCategoryAndDeck(@PathVariable Long deckId) {
        LOGGER.debug("View card by category and deck with id {}", deckId);
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        return buildDtoListForCollection(cards, CardPublicDTO.class,
                linkTo(methodOn(DeckController.class).getCardsByCategoryAndDeck(deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_COURSE)
    @GetMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks/{deckId}/cards")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','READ')")
    public List<CardPublicDTO> getCardsByCourseAndDeck(@PathVariable Long categoryId, @PathVariable Long courseId,
                                                       @PathVariable Long deckId) {
        LOGGER.debug("View card by course and deck with id {}", deckId);
        List<Card> cards = deckService.getAllCardsByDeckId(deckId);
        return buildDtoListForCollection(cards, CardPublicDTO.class, linkTo(methodOn(DeckController.class)
                .getCardsByCourseAndDeck(categoryId, courseId, deckId)).withSelfRel());
    }

    @Auditable(action = AuditingAction.CREATE_DECK_IN_COURSE)
    @PostMapping(value = "/api/courses/{courseId}/decks")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('DECK','CREATE')")
    public DeckPublicDTO addDeckToCourse(@Validated(Request.class) @RequestBody Deck deck, @PathVariable Long courseId) {
        LOGGER.debug("Adding deck to course with id {}", courseId);
        deckService.addDeckToCourse(deck, courseId);
        return buildDtoForEntity(deck, DeckPublicDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_ADMIN)
    @GetMapping(value = "/api/admin/decks")
    @PreAuthorize("hasPermission('ADMIN_DECK','READ')")
    public ResponseEntity<Page<DeckOfUserManagedByAdminDTO>> getAllDecksForAdmin(@RequestParam(name = "p", defaultValue = "1") int pageNumber,
                                                                                 @RequestParam(name = "sortBy") String sortBy,
                                                                                 @RequestParam(name = "asc") boolean ascending) {
        LOGGER.debug("View all decks for admin");
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
    @PreAuthorize("hasPermission('ADMIN_DECK','CREATE')")
    public ResponseEntity<DeckOfUserManagedByAdminDTO> addDeckForAdmin(@Validated @RequestBody DeckCreateValidationDTO deckCreateValidationDTO)
            throws NotAuthorisedUserException {
        LOGGER.debug("Adding deck for admin");
        Deck deckNew = deckService.createNewDeckAdmin(deckCreateValidationDTO);
        return new ResponseEntity<>(DTOBuilder.buildDtoForEntity(deckNew, DeckOfUserManagedByAdminDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(deckNew.getId())).withSelfRel()), HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK_ADMIN)
    @PutMapping(value = "/api/admin/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('ADMIN_DECK','UPDATE')")
    public DeckOfUserManagedByAdminDTO updateDeckForAdmin(@Validated(Request.class) @RequestBody DeckEditByAdminDTO deckEditByAdminDTO,
                                                          @PathVariable Long deckId) {
        LOGGER.debug("Updating deck for admin by id {}", deckId);
        Deck updatedDeck = deckService.updateDeckAdmin(deckEditByAdminDTO, deckId);
        return buildDtoForEntity(updatedDeck, DeckOfUserManagedByAdminDTO.class,
                linkTo(methodOn(DeckController.class).getDeckById(updatedDeck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.DELETE_DECK_ADMIN)
    @DeleteMapping(value = "/api/admin/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('ADMIN_DECK','DELETE')")
    public void deleteDeckForAdmin(@PathVariable Long deckId) throws NotAuthorisedUserException {
        LOGGER.debug("Deleting deck for admin by id {}", deckId);
        folderService.deleteDeckFromAllUsers(deckId);
        deckService.deleteDeck(deckId);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_USER)
    @DeleteMapping(value = "/api/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','DELETE') && " +
            "@deckServiceImpl.getDeck(#deckId).createdBy==principal.id")
    public List<DeckPrivateDTO> deleteOwnDeckByUser(@PathVariable Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        LOGGER.debug("Deleting own users deck by id {}", deckId);
        deckService.deleteOwnDeck(deckId);
        List<Deck> decksList = deckService.getAllDecksByUser();
        return buildDtoListForCollection(decksList, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel());
    }

    @Auditable(action = AuditingAction.CREATE_DECK_USER)
    @PostMapping(value = "/api/categories/{categoryId}/decks")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('DECK','CREATE')")
    public DeckPrivateDTO addDeckForUser(@Validated(Request.class) @RequestBody Deck deck, @PathVariable Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        LOGGER.debug("Adding deck by user to category with id {}", categoryId);
        deckService.createNewDeck(deck, categoryId);
        folderService.addDeck(deck.getId());
        return buildDtoForEntity(deck, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getOneDeckForUser(deck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.EDIT_DECK_USER)
    @PutMapping(value = "/api/categories/{categoryId}/decks/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','UPDATE') && @deckServiceImpl.getDeck(#deckId).createdBy==principal.id")
    public DeckPrivateDTO updateDeckForUser(@Validated(Request.class) @RequestBody Deck deck,
                                            @PathVariable Long deckId, @PathVariable Long categoryId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        LOGGER.debug("Updating deck with id {} for user in category with id {}", deckId, categoryId);
        Deck updatedDeck = deckService.updateOwnDeck(deck, deckId, categoryId);
        return buildDtoForEntity(updatedDeck, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getOneDeckForUser(updatedDeck.getId())).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_USER)
    @GetMapping(value = "/api/users/folders/decks/own")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','READ')")
    public List<DeckPrivateDTO> getAllDecksForUser() throws NotAuthorisedUserException {
        LOGGER.debug("View all decks for user");
        List<Deck> decksList = deckService.getAllDecksByUser();
        return buildDtoListForCollection(decksList, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel());
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_USER)
    @GetMapping(value = "/api/users/folders/decks/own/{deckId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('DECK','READ')")
    public DeckPrivateDTO getOneDeckForUser(@PathVariable Long deckId)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        LOGGER.debug("View one deck for user with id {}", deckId);
        Deck deck = deckService.getDeckUser(deckId);
        return buildDtoForEntity(deck, DeckPrivateDTO.class,
                linkTo(methodOn(DeckController.class).getOneDeckForUser(deckId)).withSelfRel());
    }
}
