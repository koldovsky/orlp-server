package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.hateoas.Link;

public class DeckOfUserManagedByAdminDTO extends DTO<Deck> {

    public DeckOfUserManagedByAdminDTO(Deck deck, Link link) {
        super(deck, link);
    }

    public Long getDeckId() { return getEntity().getId(); }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public double getRating() {
        return getEntity().getRating();
    }

    public String getCategory() {
        return getEntity().getCategory().getName();
    }

    public String getOwner() {
        return getEntity().getDeckOwner().getAccount().getEmail();
    }

}
