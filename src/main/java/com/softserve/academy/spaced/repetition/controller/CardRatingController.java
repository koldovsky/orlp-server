package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardRatingPublicDTO;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.RatingsBadValueException;
import com.softserve.academy.spaced.repetition.service.CardRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CardRatingController {

    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    @Autowired
    private CardRatingService cardRatingService;

    /**
     * Get rating of card by id.
     *
     * @param cardId
     * @return CardRatingDTO
     */
    @GetMapping("api/rate/card/{cardId}")
    public ResponseEntity<CardRatingPublicDTO> getCardRatingById(@PathVariable Long cardId) {
        CardRating cardRating = cardRatingService.getCardRatingById(cardId);
        Link selfLink = linkTo(methodOn(CardRatingController.class).getCardRatingById(cardRating.getId())).withRel("cardRating");
        CardRatingPublicDTO cardRatingDTO = DTOBuilder.buildDtoForEntity(cardRating, CardRatingPublicDTO.class, selfLink);
        return new ResponseEntity<>(cardRatingDTO, HttpStatus.OK);
    }

    /**
     * Add rating for card or rewrite last one.
     *
     * @param cardRating
     * @param deckId
     * @param cardId
     * @return CardRatingDTO
     * @throws RatingsBadValueException
     * @throws NotAuthorisedUserException
     */
    @Auditable(action = AuditingAction.RATE_CARD)
    @PostMapping("/api/private/decks/{deckId}/cards/{cardId}/rate")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#deckId, #cardId)")
    public ResponseEntity<CardRatingPublicDTO> addCardRating(@RequestBody CardRating cardRating, @PathVariable Long deckId, @PathVariable Long cardId) throws RatingsBadValueException, NotAuthorisedUserException {
        if ((cardRating.getRating() >= MIN_RATING) && (cardRating.getRating() <= MAX_RATING)) {
            cardRatingService.addCardRating(cardRating, deckId, cardId);
            Link selfLink = linkTo(methodOn(CardRatingController.class).getCardRatingById(cardRating.getId())).withSelfRel();
            CardRatingPublicDTO cardRatingPublicDTO = DTOBuilder.buildDtoForEntity(cardRating, CardRatingPublicDTO.class, selfLink);
            return new ResponseEntity<>(cardRatingPublicDTO, HttpStatus.CREATED);
        } else {
            throw new RatingsBadValueException();
        }
    }
}
