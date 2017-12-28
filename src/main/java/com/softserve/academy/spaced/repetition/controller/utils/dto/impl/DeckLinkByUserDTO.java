package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTO;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class DeckLinkByUserDTO extends DTO<Deck> {
    public DeckLinkByUserDTO(Deck deck, Link link) {
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

    public String getCategory() { return getEntity().getCategory().getName(); }

    public Long getCategoryId(){return getEntity().getCategory().getId();}

    public String getOwner() {
        return getEntity().getDeckOwner().getAccount().getEmail();
    }

}
