package com.softserve.academy.spaced.repetition.controller;
import com.softserve.academy.spaced.repetition.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.dto.RatingDTO;
import com.softserve.academy.spaced.repetition.dto.impl.DeckRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.service.impl.DeckRatingServiceImpl;
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
    private DeckRatingServiceImpl deckRatingServiceImpl;

    @GetMapping("api/deck/{deckId}/rating/{id}")
    public ResponseEntity<DeckRatingPublicDTO> getDeckRatingById(@PathVariable Long deckId, @PathVariable Long id) {
        DeckRating deckRating = deckRatingServiceImpl.getDeckRatingById(id);
        Link selfLink = linkTo(methodOn(DeckRatingController.class).getDeckRatingById(deckRating.getDeck().getId(), deckRating.getId())).withRel("deckRating");
        DeckRatingPublicDTO deckRatingDTO = DTOBuilder.buildDtoForEntity(deckRating, DeckRatingPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckRatingDTO, HttpStatus.OK);
    }

    @PostMapping("/api/private/deck/{deckId}")
    public ResponseEntity addDeckRating(@RequestBody RatingDTO ratingDTO, @PathVariable Long deckId) throws NotAuthorisedUserException, UserStatusException {
        if ((ratingDTO.getRating() >= MIN_RATING) && (ratingDTO.getRating() <= MAX_RATING)) {
            deckRatingServiceImpl.addDeckRating(ratingDTO.getRating(), deckId);
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            throw new IllegalArgumentException("Rating can't be less than 1 and more than 5");
        }
    }
}