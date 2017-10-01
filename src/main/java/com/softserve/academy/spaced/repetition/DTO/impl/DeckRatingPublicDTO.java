package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import org.springframework.hateoas.Link;

public class DeckRatingPublicDTO extends DTO<DeckRating> {

    public DeckRatingPublicDTO(DeckRating deckRating, Link link) {
        super(deckRating, link);
    }

    public String getAccountEmail() {
        return getEntity().getAccountEmail();
    }

    public long getDeckId() {
        return getEntity().getDeck().getId();
    }

    public int getRating() {
        return getEntity().getRating();
    }

}