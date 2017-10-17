package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DeckComment")
public class DeckComment extends Comment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    public DeckComment() {
    }

    public DeckComment(String commentText, Date commentDate) {
        super(commentText, commentDate);
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
