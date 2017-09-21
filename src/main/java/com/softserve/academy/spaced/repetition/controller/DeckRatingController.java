package com.softserve.academy.spaced.repetition.controller;
import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.RatingsBadValueException;
import com.softserve.academy.spaced.repetition.service.DeckRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class DeckRatingController {

    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    @Autowired
    private DeckRatingService deckRatingService;

    @GetMapping("api/rate/deck/{id}")
    public ResponseEntity<DeckRatingPublicDTO> getDeckRatingById(@PathVariable Long deckId) {
        DeckRating deckRating = deckRatingService.getDeckRatingById(deckId);
        Link selfLink = linkTo(methodOn(DeckRatingController.class).getDeckRatingById(deckRating.getId())).withRel("deckRating");
        DeckRatingPublicDTO deckRatingDTO = DTOBuilder.buildDtoForEntity(deckRating, DeckRatingPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckRatingDTO, HttpStatus.OK);
    }

    @PostMapping("/api/private/deck/{deckId}")
    public ResponseEntity<DTO<DeckRating>> addDeckRating(@RequestBody DeckRating deckRating, @PathVariable Long deckId) throws RatingsBadValueException, NotAuthorisedUserException {
        if ((deckRating.getRating() >= MIN_RATING) && (deckRating.getRating() <= MAX_RATING)) {
            deckRatingService.addDeckRating(deckRating, deckId);
            Link selfLink = linkTo(methodOn(DeckRatingController.class).getDeckRatingById(deckRating.getId())).withSelfRel();
            DeckRatingPublicDTO deckRatingPublicDTO = DTOBuilder.buildDtoForEntity(deckRating, DeckRatingPublicDTO.class, selfLink);
            return new ResponseEntity<>(deckRatingPublicDTO, HttpStatus.CREATED);
        } else {
            throw new RatingsBadValueException();
        }
    }
}