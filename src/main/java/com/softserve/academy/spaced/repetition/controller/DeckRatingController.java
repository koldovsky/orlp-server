package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.service.DeckRatingService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/decks/{deckId}/ratings")
public class DeckRatingController {

    @Autowired
    private DeckRatingService deckRatingService;

    @GetMapping("{id}")
    public ResponseEntity<DeckRatingPublicDTO> getDeckRatingById(@PathVariable Long id) {
        DeckRating deckRating = deckRatingService.getDeckRatingById(id);
        Link selfLink = linkTo(methodOn(DeckRatingController.class)
                .getDeckRatingById(deckRating.getId())).withRel("deckRating");
        DeckRatingPublicDTO deckRatingDTO = buildDtoForEntity(deckRating, DeckRatingPublicDTO.class, selfLink);
        return new ResponseEntity<>(deckRatingDTO, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeckRatingPublicDTO addDeckRating(@Validated(Request.class) @RequestBody DeckRating deckRating,
                                             @PathVariable Long deckId)
            throws NotAuthorisedUserException, UserStatusException {
        deckRating = deckRatingService.addDeckRating(deckRating.getRating(), deckId);
                return buildDtoForEntity(deckRating, DeckRatingPublicDTO.class,
                                linkTo(methodOn(DeckRatingController.class)
                                               .getDeckRatingById(deckRating.getId())).withSelfRel());
    }
}