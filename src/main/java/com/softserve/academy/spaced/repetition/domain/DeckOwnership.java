package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "deck_ownership")
public class DeckOwnership  extends Ownership {

    @Column(name = "deck_id")
    private Long deckId;

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }
}
