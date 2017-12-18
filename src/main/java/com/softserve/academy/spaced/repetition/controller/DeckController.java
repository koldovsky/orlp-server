package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.dto.impl.*;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.service.impl.DeckServiceImpl;
import com.softserve.academy.spaced.repetition.service.impl.FolderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class DeckController {
    @Autowired
    private DeckServiceImpl deckServiceImpl;

    @Autowired
    private FolderServiceImpl folderServiceImpl;

    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_CATEGORY)
    @GetMapping(value = "/api/category/{category_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id)")
    public ResponseEntity<Page<DeckLinkByCategoryDTO>> getAllDecksByCategoryId(@PathVariable Long category_id,@RequestParam(name = "p", defaultValue = "1") int pageNumber, @RequestParam(name = "sortBy") String sortBy, @RequestParam(name = "asc") boolean ascending) {
        Page<DeckLinkByCategoryDTO> deckByCategoryDTOS = deckServiceImpl.getPageWithDecksByCategory(category_id,pageNumber, sortBy, ascending).map((deck) -> {
            Link selfLink = linkTo(methodOn(DeckController.class).getAllDecksByCategoryId(category_id,pageNumber,sortBy,ascending)).withRel("deck");
            return DTOBuilder.buildDtoForEntity(deck, DeckLinkByCategoryDTO.class, selfLink);
        });
        return new ResponseEntity<>(deckByCategoryDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/ordered")
    public ResponseEntity<List<DeckPublicDTO>> getAllDecksOrderByRating() {
        List<Deck> decksList = deckServiceImpl.getAllOrderedDecks();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksOrderByRating()).withRel("deck");
        List<DeckPublicDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckPublicDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_COURSE)
    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCourse(#category_id, #course_id)")
    public ResponseEntity<List<DeckLinkByCourseDTO>> getAllDecksByCourseId(@PathVariable Long category_id, @PathVariable Long course_id) {
        List<Deck> decksList = deckServiceImpl.getAllDecks(course_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksByCourseId(category_id, course_id)).withRel("course");
        List<DeckLinkByCourseDTO> decks = DTOBuilder.buildDtoListForCollection(decksList, DeckLinkByCourseDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromCategory(#category_id, #deck_id)")
    public ResponseEntity<DeckLinkByCategoryDTO> getDeckByCategoryId(@PathVariable Long category_id, @PathVariable Long deck_id) {
        Deck deck = deckServiceImpl.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(category_id, deck_id)).withSelfRel();
        DeckLinkByCategoryDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByCategoryDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id, #course_id, #deck_id)")
    public ResponseEntity<DeckLinkByCourseDTO> getDeckByCourseId(@PathVariable Long category_id, @PathVariable Long course_id,
                                                                 @PathVariable Long deck_id) {
        Deck deck = deckServiceImpl.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCourseId(category_id, course_id, deck_id)).withSelfRel();
        DeckLinkByCourseDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByCourseDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_CATEGORY)
    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromCategory(#category_id, #deck_id)")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCategoryAndDeck(@PathVariable Long category_id, @PathVariable Long deck_id) {
        List<Card> cards = deckServiceImpl.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByCategoryAndDeck(category_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/{deck_id}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByDeck(@PathVariable Long deck_id) {
        List<Card> cards = deckServiceImpl.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByDeck(deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_COURSE)
    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id, #course_id, #deck_id)")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCourseAndDeck(@PathVariable Long category_id, @PathVariable Long course_id, @PathVariable Long deck_id) {
        List<Card> cards = deckServiceImpl.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByCourseAndDeck(category_id, course_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_IN_CATEGORY)
    @PostMapping(value = "/api/category/{category_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id)")
    public ResponseEntity<DeckPublicDTO> addDeckToCategory(@RequestBody Deck deck, @PathVariable Long category_id) {
        deckServiceImpl.addDeckToCategory(deck, category_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(category_id, deck.getId())).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_IN_COURSE)
    @PostMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCourse(#categoryId, #courseId)")
    public ResponseEntity<DeckPublicDTO> addDeckToCourse(@RequestBody Deck deck,
                                                         @PathVariable Long categoryId, @PathVariable Long courseId) {
        deckServiceImpl.addDeckToCourse(deck, categoryId, courseId);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCourseId(categoryId, courseId, deck.getId())).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK)
    @PutMapping(value = "/api/user/{userId}/decks/{deckId}/{categoryId}")
    public void updateDeck(@RequestBody Deck deck, @PathVariable Long deckId, @PathVariable Long categoryId) {
        deckServiceImpl.updateDeck(deck, deckId, categoryId);
    }

    @Auditable(action = AuditingAction.DELETE_DECK)
    @DeleteMapping(value = "/api/user/{userId}/decks/{deckId}")
    public void deleteDeck(@PathVariable Long deckId) {
        deckServiceImpl.deleteDeck(deckId);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_ADMIN)
    @GetMapping(value = "/api/admin/decks")
    public ResponseEntity<Page<DeckOfUserManagedByAdminDTO>> getAllDecksForAdmin(@RequestParam(name = "p", defaultValue = "1") int pageNumber, @RequestParam(name = "sortBy") String sortBy, @RequestParam(name = "asc") boolean ascending) {
        Page<DeckOfUserManagedByAdminDTO> deckOfUserManagedByAdminDTO = deckServiceImpl.getPageWithAllAdminDecks(pageNumber, sortBy, ascending).map((deck) -> {
            Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(deck.getId())).withSelfRel();
            return DTOBuilder.buildDtoForEntity(deck, DeckOfUserManagedByAdminDTO.class, selfLink);
        });
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_ADMIN)
    @GetMapping(value = "/api/admin/decks/{deckId}")
    public ResponseEntity<DeckOfUserManagedByAdminDTO> getOneDeckForAdmin(@PathVariable Long deckId){
        Deck deck = deckServiceImpl.getDeck(deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(deckId)).withSelfRel();
        DeckOfUserManagedByAdminDTO deckOfUserManagedByAdminDTO = DTOBuilder.buildDtoForEntity(deck,
                DeckOfUserManagedByAdminDTO.class,selfLink);
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO,HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_ADMIN)
    @PostMapping(value = "/api/admin/decks")
    public ResponseEntity<DeckOfUserManagedByAdminDTO> addDeckForAdmin(@RequestBody Deck deck) throws NotAuthorisedUserException {
        Deck deckNew = deckServiceImpl.createNewDeckAdmin(deck);
        folderServiceImpl.addDeck(deckNew.getId());
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(deckNew.getId())).withSelfRel();
        DeckOfUserManagedByAdminDTO deckOfUserManagedByAdminDTO = DTOBuilder.buildDtoForEntity(deckNew, DeckOfUserManagedByAdminDTO.class, selfLink);
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK_ADMIN)
    @PutMapping(value = "/api/admin/decks/{deckId}")
    public ResponseEntity updateDeckForAdmin(@RequestBody Deck deck, @PathVariable Long deckId) {
        Deck updatedDeck= deckServiceImpl.updateDeckAdmin(deck, deckId);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(updatedDeck.getId())).withSelfRel();
        DeckOfUserManagedByAdminDTO deckOfUserManagedByAdminDTO = DTOBuilder.buildDtoForEntity(updatedDeck, DeckOfUserManagedByAdminDTO.class, selfLink);
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_ADMIN)
    @DeleteMapping(value = "/api/admin/decks/{deckId}")
    public ResponseEntity deleteDeckForAdmin(@PathVariable Long deckId)throws NotAuthorisedUserException {
        folderServiceImpl.deleteDeckFromAllUsers(deckId);
        deckServiceImpl.deleteDeck(deckId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_USER)
    @DeleteMapping(value = "/api/private/deck/{deck_id}")
    public ResponseEntity<List<DeckPrivateDTO>> deleteOwnDeckByUser(@PathVariable Long deck_id)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        deckServiceImpl.deleteOwnDeck(deck_id);
        List<Deck> decksList = deckServiceImpl.getAllDecksByUser();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel();
        List<DeckPrivateDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckPrivateDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_USER)
    @PostMapping(value = "/api/private/category/{categoryId}/decks")
    public ResponseEntity<DeckPrivateDTO> addDeckForUser(@RequestBody Deck deck, @PathVariable Long categoryId) throws NotAuthorisedUserException, NotOwnerOperationException {
        deckServiceImpl.createNewDeck(deck, categoryId);
        folderServiceImpl.addDeck(deck.getId());
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForUser(deck.getId())).withSelfRel();
        DeckPrivateDTO deckDTO = DTOBuilder.buildDtoForEntity(deck, DeckPrivateDTO.class, selfLink);
        return new ResponseEntity<>(deckDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK_USER)
    @PutMapping(value = "/api/private/category/{categoryId}/deck/{deckId}")
    public ResponseEntity<DeckPrivateDTO> updateDeckForUser(@RequestBody Deck deck, @PathVariable Long deckId, @PathVariable Long categoryId) throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck updatedDeck = deckServiceImpl.updateOwnDeck(deck, deckId, categoryId);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForUser(updatedDeck.getId())).withSelfRel();
        DeckPrivateDTO deckDTO = DTOBuilder.buildDtoForEntity(updatedDeck, DeckPrivateDTO.class, selfLink);
        return new ResponseEntity<>(deckDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_USER)
    @GetMapping(value = "/api/private/user/folder/decks/own")
    public ResponseEntity<List<DeckPrivateDTO>> getAllDecksForUser() throws NotAuthorisedUserException {
        List<Deck> decksList = deckServiceImpl.getAllDecksByUser();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksForUser()).withSelfRel();
        List<DeckPrivateDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckPrivateDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_USER)
    @GetMapping(value = "/api/private/user/folder/decks/own/{deck_id}")
    public ResponseEntity<DeckPrivateDTO> getOneDeckForUser(@PathVariable Long deck_id)
            throws NotAuthorisedUserException, NotOwnerOperationException {
        Deck deck = deckServiceImpl.getDeckUser(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForUser(deck_id)).withSelfRel();
        DeckPrivateDTO deckDTO = DTOBuilder.buildDtoForEntity(deck, DeckPrivateDTO.class, selfLink);
        return new ResponseEntity<>(deckDTO, HttpStatus.OK);
    }

}
