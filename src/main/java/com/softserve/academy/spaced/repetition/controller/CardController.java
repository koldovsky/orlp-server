package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.service.CardService;
import com.softserve.academy.spaced.repetition.service.cardLoaders.CardLoadService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.WrongFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardService cardService;

    @Autowired
    private CardLoadService cardLoadService;

    @GetMapping("/api/decks/{deckId}/learn")
    public ResponseEntity<List<CardPublicDTO>> getLearningCards(@PathVariable Long deckId)
            throws NotAuthorisedUserException {
        List<Card> learningCards = cardService.getLearningCards(deckId);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByDeck(deckId)).withSelfRel();
        List<CardPublicDTO> cards = DTOBuilder
                .buildDtoListForCollection(learningCards, CardPublicDTO.class, collectionLink);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/api/private/decks/{deckId}/learn/additional")
    public ResponseEntity<List<CardPublicDTO>> getAdditionalLearningCards(@PathVariable Long deckId)
            throws NotAuthorisedUserException {
        List<Card> learningCards = cardService.getAdditionalLearningCards(deckId);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByDeck(deckId)).withSelfRel();
        List<CardPublicDTO> cards = DTOBuilder
                .buildDtoListForCollection(learningCards, CardPublicDTO.class, collectionLink);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("api/private/decks/{deckId}/not-postponed")
    public ResponseEntity<Boolean> areThereNotPostponedCardsAvailable(@PathVariable Long deckId)
            throws NotAuthorisedUserException {
        return ResponseEntity.ok(cardService.areThereNotPostponedCardsAvailable(deckId));
    }

    @GetMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks/{deckId}/cards/{cardId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#categoryId, #courseId, #deckId, #cardId)")
    public ResponseEntity<CardPublicDTO> getCardByCourseAndDeck(@PathVariable Long categoryId,
                                                                @PathVariable Long courseId,
                                                                @PathVariable Long deckId,
                                                                @PathVariable Long cardId) {
        Card card = cardService.getCard(cardId);
        Link selfLink = linkTo(methodOn(CardController.class)
                .getCardByCourseAndDeck(categoryId, courseId, deckId, cardId)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{categoryId}/decks/{deckId}/cards/{cardId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#categoryId, #deckId, #cardId)")
    public ResponseEntity<CardPublicDTO> getCardByCategoryAndDeck(@PathVariable Long categoryId,
                                                                  @PathVariable Long deckId,
                                                                  @PathVariable Long cardId) {
        Card card = cardService.getCard(cardId);
        Link selfLink = linkTo(methodOn(CardController.class)
                .getCardByCategoryAndDeck(categoryId, deckId, cardId)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/decks/{deckId}/cards/{cardId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#deckId, #cardId)")
    public ResponseEntity<CardPublicDTO> getCardByDeck(@PathVariable Long deckId, @PathVariable Long cardId) {
        Card card = cardService.getCard(cardId);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByDeck(deckId, cardId)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }


    @Auditable(action = AuditingAction.CREATE_CARD_VIA_COURSE_AND_DECK)
    @PostMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks/{deckId}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#categoryId, #courseId, #deckId)")
    public ResponseEntity<CardPublicDTO> addCardByCourseAndDeck(@Validated(Request.class) @RequestBody Card card,
                                                                @PathVariable Long categoryId,
                                                                @PathVariable Long courseId,
                                                                @PathVariable Long deckId) {
        cardService.addCard(card, deckId);
        Link selfLink = linkTo(methodOn(CardController.class)
                .getCardByCourseAndDeck(categoryId, courseId, deckId, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.CREATE_CARD_VIA_CATEGORY_AND_DECK)
    @PostMapping(value = "/api/category/{categoryId}/decks/{deckId}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromCategory(#categoryId, #deckId)")
    public ResponseEntity<CardPublicDTO> addCardByCategoryAndDeck(@Validated(Request.class) @RequestBody Card card,
                                                                  @PathVariable Long categoryId,
                                                                  @PathVariable Long deckId) {
        LOGGER.debug("Add card to categoryId: {}, deckId: {}", categoryId, deckId);
        cardService.addCard(card, deckId);
        Link selfLink = linkTo(methodOn(CardController.class)
                .getCardByCategoryAndDeck(categoryId, deckId, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_CARD_VIA_COURSE_AND_DECK)
    @PutMapping(value = "/api/category/{categoryId}/courses/{courseId}/decks/{deckId}/cards/{cardId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#categoryId, #courseId, #deckId, #cardId)")
    public ResponseEntity<CardPublicDTO> updateCardByCourseAndDeck(@PathVariable Long categoryId,
                                                                   @PathVariable Long courseId,
                                                                   @PathVariable Long deckId,
                                                                   @PathVariable Long cardId,
                                                                   @Validated(Request.class) @RequestBody Card card) {
        cardService.updateCard(cardId, card);
        Link selfLink = linkTo(methodOn(CardController.class)
                .getCardByCourseAndDeck(categoryId, courseId, deckId, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.EDIT_CARD_VIA_CATEGORY_AND_DECK)
    @PutMapping(value = "/api/category/{categoryId}/decks/{deckId}/cards/{cardId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#categoryId, #deckId, #cardId)")
    public ResponseEntity<CardPublicDTO> updateCardByCategoryAndDeck(@PathVariable Long categoryId,
                                                                     @PathVariable Long deckId,
                                                                     @PathVariable Long cardId,
                                                                     @Validated(Request.class) @RequestBody Card card) {
        cardService.updateCard(cardId, card);
        Link selfLink = linkTo(methodOn(CardController.class)
                .getCardByCategoryAndDeck(categoryId, deckId, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.EDIT_CARD_VIA_CATEGORY_AND_DECK)
    @PutMapping(value = "/api/decks/{deckId}/cards/{cardId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#deckId, #cardId)")
    public ResponseEntity<CardPublicDTO> updateCardByDeck(@PathVariable Long deckId,
                                                          @PathVariable Long cardId,
                                                          @Validated(Request.class) @RequestBody Card card) {
        LOGGER.debug("Updating card with id: {}  in deck with id: {}", cardId, deckId);
        cardService.updateCard(cardId, card);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByDeck(deckId, cardId)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_CARD)
    @DeleteMapping(value = {"/api/category/{categoryId}/decks/{deckId}/cards/{cardId}",
            "/api/decks/{deckId}/cards/{cardId}", "/api/courses/{courseId}/decks/{deckId}/cards/{cardId}"})
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

    @GetMapping("/api/category/{categoryId}/decks/{deckId}/learn/cards")
    public ResponseEntity<List<CardPublicDTO>> getLearningCards(@PathVariable long categoryId, @PathVariable long deckId)
            throws NotAuthorisedUserException {
        List<Card> learningCards = cardService.getCardsQueue(deckId);
        Link collectionLink = linkTo(methodOn(DeckController.class)
                .getCardsByCategoryAndDeck(categoryId, deckId)).withSelfRel();
        List<CardPublicDTO> cards = DTOBuilder
                .buildDtoListForCollection(learningCards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    /**
     * Upload anki cards
     *
     * @param file - card-file
     * @return - HttpStatus.Ok
     * @throws NoSuchElementException - is dropping when classloader failed in loading Driver to uploading file.
     * @throws WrongFormatException   - is dropping when uploading file has wrong format.
     * @throws NoSuchElementException - is dropping when file is not found.
     */
    @PostMapping("/api/cardsUpload")
    public ResponseEntity uploadCard(@RequestParam("file") MultipartFile file, Long deckId)
            throws IOException, SQLException, ClassNotFoundException, WrongFormatException {
        cardLoadService.loadCard(file, deckId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/api/card/{cardId}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#cardId)")
    public ResponseEntity<CardPublicDTO> getCardById(@PathVariable Long cardId) {
        Card card = cardService.getCard(cardId);
        Link selfLink = linkTo(methodOn(CardController.class).getCardById(cardId)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardPublicDTO, HttpStatus.OK);
    }
}
