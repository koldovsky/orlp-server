package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "DECK")
public class DeckOwnership  extends Ownership {
    @Column(name = "deck_id")
    private Long deckId;

    public DeckOwnership() {
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }
}
