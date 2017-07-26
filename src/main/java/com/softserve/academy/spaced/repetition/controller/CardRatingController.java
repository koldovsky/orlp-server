package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.exceptions.MoreThanOneTimeRateException;
import com.softserve.academy.spaced.repetition.exceptions.RatingsBadValueException;
import com.softserve.academy.spaced.repetition.service.CardRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CardRatingController {

    @Autowired
    private CardRatingService cardRatingService;


    @GetMapping("api/rate/card")
    public ResponseEntity<List<CardRatingPublicDTO>> getCardRating() {
        try {
            List<CardRating> cardRatingsList = cardRatingService.getAllCardRating();
            Link collectionLink = linkTo(methodOn(CardRatingController.class).getCardRating()).withRel("card");
            List<CardRatingPublicDTO> cardRatings = DTOBuilder.buildDtoListForCollection(cardRatingsList, CardRatingPublicDTO.class, collectionLink);
            return new ResponseEntity<>(cardRatings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("api/rate/card/{id}")
    public ResponseEntity<CardRatingPublicDTO> getCardRatingById(@PathVariable Long id) {
        try {
            CardRating cardRating = cardRatingService.getCardRatingById(id);
            Link selfLink = linkTo(methodOn(CardRatingController.class).getCardRatingById(cardRating.getId())).withRel("cardRating");
            CardRatingPublicDTO cardRatingDTO = DTOBuilder.buildDtoForEntity(cardRating, CardRatingPublicDTO.class, selfLink);
            return new ResponseEntity<>(cardRatingDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/category/{categoryId}/decks/{deckId}/cards/{cardId}/rate")
    public ResponseEntity<DTO<CardRating>> addCardRating(@RequestBody CardRating cardRating, @PathVariable Long deckId, @PathVariable Long cardId) throws MoreThanOneTimeRateException,RatingsBadValueException {

        if (cardRating.getRating() <= 5 && cardRating.getRating() >= 0) {
            cardRatingService.addCardRating(cardRating, deckId, cardId);
            Link selfLink = linkTo(methodOn(CardRatingController.class).getCardRatingById(cardRating.getId())).withSelfRel();
            CardRatingPublicDTO cardRatingPublicDTO = DTOBuilder.buildDtoForEntity(cardRating, CardRatingPublicDTO.class, selfLink);
            return new ResponseEntity<>(cardRatingPublicDTO, HttpStatus.CREATED);
        } else
            throw new RatingsBadValueException();
    }

}
