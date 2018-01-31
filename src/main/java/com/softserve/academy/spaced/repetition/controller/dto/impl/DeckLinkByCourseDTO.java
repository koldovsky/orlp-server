package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class DeckLinkByCourseDTO extends DTO<Deck> {
    public DeckLinkByCourseDTO(Deck deck, Link link) {
        super(deck, link);
        Link linkCards = linkTo(methodOn(DeckController.class).getCardsByCourseAndDeck((long) -1, (long) -1, getEntity().getId())).withRel("cards");
        add(getLinkWithReplacedParentPart(linkCards).withRel("cards"));
    }

    public Long getDeckId() { return getEntity().getId(); }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public Double getRating() {
        return getEntity().getRating();
    }
}
