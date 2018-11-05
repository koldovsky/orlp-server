package com.softserve.academy.spaced.repetition.controller.dto.impl;


import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.controller.dto.builder.SearchDTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.enums.SearchType;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class DeckSearchDTO extends SearchDTO<Deck> {

    public DeckSearchDTO(Deck entity) {
        super(entity);
    }

    @Override
    public String getDescription() { return getEntity().getDescription(); }

    @Override
    public String getName() { return getEntity().getName(); }

    public double getRating() { return getEntity().getRating(); }

    @Override
    public String getResultType() { return SearchType.DECK.toString(); }

    @Override
    public String getSelfLink() {
        return linkTo(methodOn(DeckController.class).getDeckById(getEntity().getId())).withSelfRel().getHref();
    }
}
