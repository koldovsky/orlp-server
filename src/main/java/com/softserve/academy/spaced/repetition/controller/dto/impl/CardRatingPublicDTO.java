package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import org.springframework.hateoas.Link;

public class CardRatingPublicDTO extends DTO<CardRating> {

    public CardRatingPublicDTO(CardRating cardRating, Link link) {
        super(cardRating, link);
    }

    public String getAccountEmail() {
        return getEntity().getAccountEmail();
    }

    public long getCardId() {
        return getEntity().getCard().getId();
    }

    public int getRating() {
        return getEntity().getRating();
    }

}
