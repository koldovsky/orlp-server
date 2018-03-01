package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckRatingPublicDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.RatingDTO;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.service.DeckRatingService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/decks/{deckId}")
public class DeckRatingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private DeckRatingService deckRatingService;

    @GetMapping("/ratings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeckRatingPublicDTO getDeckRatingById(@PathVariable Long deckId, @PathVariable Long id) {
        DeckRating deckRating = deckRatingService.getDeckRatingById(id);
        return buildDtoForEntity(deckRating, DeckRatingPublicDTO.class, linkTo(methodOn(DeckRatingController.class)
                        .getDeckRatingById(deckRating.getDeck().getId(), deckRating.getId())).withSelfRel());
    }

    @PostMapping
    @PreAuthorize("hasPermission('DECK_RATING','CREATE')")
    public ResponseEntity addDeckRating(@Validated(Request.class) @RequestBody RatingDTO ratingDTO,
                                        @PathVariable Long deckId)
            throws NotAuthorisedUserException, UserStatusException {
        LOGGER.debug("Adding rating to deck with id: {}", deckId);
        deckRatingService.addDeckRating(ratingDTO.getRating(), deckId);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}