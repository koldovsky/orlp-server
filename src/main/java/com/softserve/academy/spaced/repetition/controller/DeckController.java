package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.*;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NoSuchDeckException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.service.DeckService;
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
public class DeckController {
    @Autowired
    private DeckService deckService;

    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_CATEGORY)
    @GetMapping(value = "/api/category/{category_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id)")
    public ResponseEntity<List<DeckLinkByCategoryDTO>> getAllDecksByCategoryId(@PathVariable Long category_id) {
        List<Deck> decksList = deckService.getAllDecksByCategory(category_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksByCategoryId(category_id)).withRel("deck");
        List<DeckLinkByCategoryDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckLinkByCategoryDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/ordered")
    public ResponseEntity<List<DeckPublicDTO>> getAllDecksOrderByRating() {
        List<Deck> decksList = deckService.getAllOrderedDecks();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksOrderByRating()).withRel("deck");
        List<DeckPublicDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckPublicDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_VIA_COURSE)
    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCourse(#category_id, #course_id)")
    public ResponseEntity<List<DeckLinkByCourseDTO>> getAllDecksByCourseId(@PathVariable Long category_id, @PathVariable Long course_id) {
        List<Deck> decksList = deckService.getAllDecks(course_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksByCourseId(category_id, course_id)).withRel("course");
        List<DeckLinkByCourseDTO> decks = DTOBuilder.buildDtoListForCollection(decksList, DeckLinkByCourseDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromCategory(#category_id, #deck_id)")
    public ResponseEntity<DeckLinkByCategoryDTO> getDeckByCategoryId(@PathVariable Long category_id, @PathVariable Long deck_id) {
        Deck deck = deckService.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(category_id, deck_id)).withSelfRel();
        DeckLinkByCategoryDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByCategoryDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id, #course_id, #deck_id)")
    public ResponseEntity<DeckLinkByCourseDTO> getDeckByCourseId(@PathVariable Long category_id, @PathVariable Long course_id,
                                                                 @PathVariable Long deck_id) {
        Deck deck = deckService.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCourseId(category_id, course_id, deck_id)).withSelfRel();
        DeckLinkByCourseDTO linkDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByCourseDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_CATEGORY)
    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromCategory(#category_id, #deck_id)")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCategoryAndDeck(@PathVariable Long category_id, @PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByCategoryAndDeck(category_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/{deck_id}/cards")
    public ResponseEntity<List<CardPublicDTO>> getCardsByDeck(@PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByDeck(deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.START_LEARNING_DECK_VIA_COURSE)
    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id, #course_id, #deck_id)")
    public ResponseEntity<List<CardPublicDTO>> getCardsByCourseAndDeck(@PathVariable Long category_id, @PathVariable Long course_id, @PathVariable Long deck_id) {
        List<Card> cards = deckService.getAllCardsByDeckId(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByCourseAndDeck(category_id, course_id, deck_id)).withSelfRel();
        List<CardPublicDTO> cardsPublic = DTOBuilder.buildDtoListForCollection(cards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cardsPublic, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_IN_CATEGORY)
    @PostMapping(value = "/api/category/{category_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id)")
    public ResponseEntity<DeckPublicDTO> addDeckToCategory(@RequestBody Deck deck, @PathVariable Long category_id) {
        deckService.addDeckToCategory(deck, category_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCategoryId(category_id, deck.getId())).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_IN_COURSE)
    @PostMapping(value = "/api/category/{category_id}/courses/{course_id}/decks")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCourse(#category_id, #course_id)")
    public ResponseEntity<DeckPublicDTO> addDeckToCourse(@RequestBody Deck deck, @PathVariable Long category_id, @PathVariable Long course_id) {
        deckService.addDeckToCourse(deck, category_id, course_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getDeckByCourseId(category_id, course_id, deck.getId())).withSelfRel();
        DeckPublicDTO deckPublicDTO = DTOBuilder.buildDtoForEntity(deck, DeckPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK)
    @PutMapping(value = "/api/user/{user_id}/decks/{deck_id}/{category_id}")
    public void updateDeck(@RequestBody Deck deck, @PathVariable Long deck_id, @PathVariable Long category_id) {
        deckService.updateDeck(deck, deck_id, category_id);
    }

    @Auditable(action = AuditingAction.DELETE_DECK)
    @DeleteMapping(value = "/api/user/{user_id}/decks/{deck_id}")
    public void deleteDeck(@PathVariable Long deck_id) {
        deckService.deleteDeck(deck_id);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_ADMIN)
    @GetMapping(value = "/api/admin/decks")
    public ResponseEntity<List<DeckOfUserManagedByAdminDTO>> getAllDecksForAdmin() {
        List<Deck> decksList = deckService.getAllOrderedDecks();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksForAdmin()).withSelfRel();
        List<DeckOfUserManagedByAdminDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckOfUserManagedByAdminDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_ADMIN)
    @GetMapping(value = "/api/admin/decks/{deck_id}")
    public ResponseEntity<DeckOfUserManagedByAdminDTO> getOneDeckForAdmin(@PathVariable Long deck_id){
        Deck deck = deckService.getDeck(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForAdmin(deck_id)).withSelfRel();
        DeckOfUserManagedByAdminDTO deckOfUserManagedByAdminDTO = DTOBuilder.buildDtoForEntity(deck, DeckOfUserManagedByAdminDTO.class,selfLink);
        return new ResponseEntity<>(deckOfUserManagedByAdminDTO,HttpStatus.OK);
    }


    @Auditable(action = AuditingAction.CREATE_DECK_ADMIN)
    @PostMapping(value = "/api/admin/decks/{category_id}")
    public ResponseEntity<Deck> addDeckForAdmin(@RequestBody Deck deck, @PathVariable Long category_id) throws NotAuthorisedUserException {
        deckService.createNewDeck(deck, category_id);
        return new ResponseEntity<>(deck, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK_ADMIN)
    @PutMapping(value = "/api/admin/decks/{deck_id}/{category_id}")
    public ResponseEntity updateDeckForAdmin(@RequestBody Deck deck, @PathVariable Long deck_id, @PathVariable Long category_id) {
        deckService.updateDeck(deck, deck_id, category_id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_ADMIN)
    @DeleteMapping(value = "/api/admin/decks/{deck_id}")
    public void deleteDeckForAdmin(@PathVariable Long deck_id) {
        deckService.deleteDeck(deck_id);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_USER)
    @DeleteMapping(value = "/api/private/deck/{deck_id}")
    public ResponseEntity<List<DeckLinkByUserDTO>> deleteOwnDeckByUser(@PathVariable Long deck_id)
            throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        deckService.deleteOwnDeck(deck_id);
        List<Deck> decksList = deckService.getAllDecksByUser();
        Link collectionLink = linkTo(methodOn(DeckController.class).deleteOwnDeckByUser(deck_id)).withRel("user");
        List<DeckLinkByUserDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckLinkByUserDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_DECK_USER)
    @PostMapping(value = "/api/private/category/{category_id}/decks")
    public  ResponseEntity<DeckLinkByUserDTO> addDeckForUser(@RequestBody Deck deck, @PathVariable Long category_id) throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        deckService.createNewDeck(deck, category_id);
        Deck deck2 = deckService.getDeckUser( deck.getId() );
        Link selfLink = linkTo(methodOn(DeckController.class).addDeckForUser(deck, category_id)).withRel("user");
        DeckLinkByUserDTO deckLinkByUserDTO = DTOBuilder.buildDtoForEntity(deck2, DeckLinkByUserDTO.class,selfLink);
        return new ResponseEntity<>(deckLinkByUserDTO,HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_DECK_USER)
    @PutMapping(value = "/api/private/category/{category_id}/deck/{deck_id}")
    public ResponseEntity updateDeckForUser(@RequestBody Deck deck, @PathVariable Long deck_id, @PathVariable Long category_id) throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        deckService.updateOwnDeck(deck, deck_id, category_id);
        Deck deck2 = deckService.getDeckUser( deck_id );
        Link selfLink = linkTo(methodOn(DeckController.class).updateDeckForUser(deck,deck_id,category_id)).withRel("user");
        DeckLinkByUserDTO deckLinkByUserDTO = DTOBuilder.buildDtoForEntity(deck2, DeckLinkByUserDTO.class,selfLink);
        return new ResponseEntity<>(deckLinkByUserDTO,HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_DECKS_USER)
    @GetMapping(value = "/api/private/user/folder/decks/own")
    public ResponseEntity<List<DeckLinkByUserDTO>> getAllDecksForUser() throws NotAuthorisedUserException {
        List<Deck> decksList = deckService.getAllDecksByUser();
        Link collectionLink = linkTo(methodOn(DeckController.class).getAllDecksForUser()).withRel("user");
        List<DeckLinkByUserDTO> decks = DTOBuilder.buildDtoListForCollection(decksList,
                DeckLinkByUserDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_ONE_DECK_USER)
    @GetMapping(value = "/api/private/user/folder/decks/own/{deck_id}")
    public ResponseEntity<DeckLinkByUserDTO> getOneDeckForUser(@PathVariable Long deck_id)
            throws NotAuthorisedUserException, NoSuchDeckException, NotOwnerOperationException {
        Deck deck = deckService.getDeckUser(deck_id);
        Link selfLink = linkTo(methodOn(DeckController.class).getOneDeckForUser(deck_id)).withRel("user");
        DeckLinkByUserDTO deckLinkByUserDTO = DTOBuilder.buildDtoForEntity(deck, DeckLinkByUserDTO.class,selfLink);
        return new ResponseEntity<>(deckLinkByUserDTO,HttpStatus.OK);
    }

}
