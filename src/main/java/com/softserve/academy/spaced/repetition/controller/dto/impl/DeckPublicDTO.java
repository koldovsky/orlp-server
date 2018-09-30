package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.hateoas.Link;

public class DeckPublicDTO extends DTO<Deck> {

    public DeckPublicDTO(Deck deck, Link link) {
        super(deck, link);
    }

    public Long getDeckId() {
        return getEntity().getId();
    }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public double getRating() {
        return getEntity().getRating();
    }

    public String getSynthax() {
        return getEntity().getSyntaxToHighlight();
    }
}
