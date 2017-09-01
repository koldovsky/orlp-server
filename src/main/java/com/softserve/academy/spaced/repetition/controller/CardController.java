package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardPublicDTO;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingActionType;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.CardService;
import com.softserve.academy.spaced.repetition.service.cardLoaders.CardLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private CardLoadService cardLoadService;

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards/{card_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#category_id, #course_id, #deck_id, #card_id)")
    public ResponseEntity <CardPublicDTO> getCardByCourseAndDeck(@PathVariable Long category_id, @PathVariable Long course_id, @PathVariable Long deck_id, @PathVariable Long card_id) {
        Card card = cardService.getCard(card_id);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCourseAndDeck(category_id, course_id, deck_id, card_id)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity <>(cardPublicDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards/{card_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#category_id, #deck_id, #card_id)")
    public ResponseEntity <CardPublicDTO> getCardByCategoryAndDeck(@PathVariable Long category_id, @PathVariable Long deck_id, @PathVariable Long card_id) {
        Card card = cardService.getCard(card_id);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCategoryAndDeck(category_id, deck_id, card_id)).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity <>(cardPublicDTO, HttpStatus.OK);
    }

    @Auditable(actionType = AuditingActionType.CREATE_CARD_VIA_COURSE_AND_DECK)
    @PostMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeck(#category_id, #course_id, #deck_id)")
    public ResponseEntity <CardPublicDTO> addCardByCourseAndDeck(@RequestBody Card card, @PathVariable Long category_id, @PathVariable Long course_id, @PathVariable Long deck_id) {
        cardService.addCard(card, deck_id);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCourseAndDeck(category_id, course_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity <>(cardPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(actionType = AuditingActionType.CREATE_CARD_VIA_CATEGORY_AND_DECK)
    @PostMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToDeckFromCategory(#category_id, #deck_id)")
    public ResponseEntity <CardPublicDTO> addCardByCategoryAndDeck(@RequestBody Card card, @PathVariable Long category_id, @PathVariable Long deck_id) {
        cardService.addCard(card, deck_id);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCategoryAndDeck(category_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity <>(cardPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(actionType = AuditingActionType.EDIT_CARD_VIA_COURSE_AND_DECK)
    @PutMapping(value = "/api/category/{category_id}/courses/{course_id}/decks/{deck_id}/cards/{card_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#category_id, #course_id, #deck_id, #card_id)")
    public ResponseEntity <CardPublicDTO> updateCardByCourseAndDeck(@PathVariable Long category_id, @PathVariable Long course_id,
                                                                    @PathVariable Long deck_id, @PathVariable Long card_id, @RequestBody Card card) {
        cardService.updateCard(card_id, card);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCourseAndDeck(category_id, course_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity <>(cardPublicDTO, HttpStatus.OK);
    }

    @Auditable(actionType = AuditingActionType.EDIT_CARD_VIA_CATEGORY_AND_DECK)
    @PutMapping(value = "/api/category/{category_id}/decks/{deck_id}/cards/{card_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#category_id, #deck_id, #card_id)")
    public ResponseEntity <CardPublicDTO> updateCardByCategoryAndDeck(@PathVariable Long category_id, @PathVariable Long deck_id, @PathVariable Long card_id, @RequestBody Card card) {
        cardService.updateCard(card_id, card);
        Link selfLink = linkTo(methodOn(CardController.class).getCardByCategoryAndDeck(category_id, deck_id, card.getId())).withSelfRel();
        CardPublicDTO cardPublicDTO = DTOBuilder.buildDtoForEntity(card, CardPublicDTO.class, selfLink);
        return new ResponseEntity <>(cardPublicDTO, HttpStatus.OK);
    }

    @Auditable(actionType = AuditingActionType.DELETE_CARD)
    @DeleteMapping(value = {"/api/category/{categoryId}/decks/{deckId}/cards/{id}", "/api/courses/{courseId}/decks/{deckId}/cards/{id}"})
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

    @GetMapping("/api/category/{category_id}/decks/{deck_id}/learn/cards")
    public ResponseEntity <List <CardPublicDTO>> getLearningCards(@PathVariable long category_id, @PathVariable long deck_id) {
        List <Card> learningCards = cardService.getCardsQueue(deck_id);
        Link collectionLink = linkTo(methodOn(DeckController.class).getCardsByCategoryAndDeck(category_id, deck_id)).withSelfRel();
        List <CardPublicDTO> cards = DTOBuilder.buildDtoListForCollection(learningCards, CardPublicDTO.class, collectionLink);
        return new ResponseEntity <>(cards, HttpStatus.OK);
    }

    /**
     * Upload anki cards
     *
     * @param file - image-file
     * @return - upploaded image DTO, HttpStatus
     * @throws ImageRepositorySizeQuotaExceededException - is dropping when user has exceeded the quote of disk-space for his own images
     * @throws NotAuthorisedUserException                - is dropping when the user which wants to add the image is not authorised
     */
    @PostMapping("/api/cardsUpload")
    public ResponseEntity <?> uploadCard(@RequestParam("file") MultipartFile file) {
        cardLoadService.loadCard(file);
        return new ResponseEntity <>(HttpStatus.OK);
    }
}
