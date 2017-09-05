package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import org.springframework.hateoas.Link;

public class CardRatingPublicDTO extends DTO<CardRating> {

    public CardRatingPublicDTO(CardRating cardRating, Link link) {
        super(cardRating, link);
    }

    public String getAccountEmail() {
        return getEntity().getAccountEmail();
    }

    public long getDeckId() {
        return getEntity().getDeckId();
    }

    public long getCardId() {
        return getEntity().getCardId();
    }

    public int getRating() {
        return getEntity().getRating();
    }

}
