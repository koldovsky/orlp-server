package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.hateoas.Link;

import java.util.List;

public class DeckPublicDTO extends DTO<Deck> {
    public DeckPublicDTO(Deck deck) {
        super(deck);
    }

    public String getName() {
        return getEntity().getName();
    }

    public String getDescription() {
        return getEntity().getDescription();
    }

    public List<Link> getLinks(){
        return getEntity().getLinks();
    }
}
