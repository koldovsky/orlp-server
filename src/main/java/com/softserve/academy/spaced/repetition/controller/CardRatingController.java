package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CardRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.service.CardRatingService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/decks/{deckId}/cards/{cardId}/rate")
public class CardRatingController {

    @Autowired
    private CardRatingService cardRatingService;

    /**
     * Get rating of card by id.
     *
     * @param cardId
     * @return CardRatingDTO
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public CardRatingPublicDTO getCardRatingById(@PathVariable Long cardId) {
        CardRating cardRating = cardRatingService.getCardRatingById(cardId);
        Link selfLink = linkTo(methodOn(CardRatingController.class)
                .getCardRatingById(cardRating.getId())).withRel("cardRating");
        return buildDtoForEntity(cardRating, CardRatingPublicDTO.class, selfLink);
    }

    /**
     * Add rating for card or rewrite last one.
     *
     * @param cardRating
     * @param cardId
     * @return CardRatingDTO
     * @throws IllegalArgumentException
     * @throws NotAuthorisedUserException
     */
    @Auditable(action = AuditingAction.RATE_CARD)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CardRatingPublicDTO addCardRating(@Validated(Request.class) @RequestBody CardRating cardRating,
                                             @PathVariable Long cardId) throws NotAuthorisedUserException {
        cardRatingService.addCardRating(cardRating, cardId);
        Link selfLink = linkTo(methodOn(CardRatingController.class).getCardRatingById(cardRating.getId())).withSelfRel();
        return buildDtoForEntity(cardRating, CardRatingPublicDTO.class, selfLink);
    }
}
