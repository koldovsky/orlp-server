package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.controller.DeckController;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class DeckPrivateDTO extends DTO<Deck> {
    public DeckPrivateDTO(Deck deck, Link link) {
        super(deck, link);
        add(linkTo(methodOn(DeckController.class).getCardsByDeck(getEntity().getId())).withRel("cards"));
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
    public String getSynthax() {return getEntity().getSyntaxToHighlight();}
}
