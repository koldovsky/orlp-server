package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "deck_price")
public class DeckPrice implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deck_price_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @Column(name = "deck_price")
    private int deckPrice;

    public DeckPrice() {
    }

    public DeckPrice(Deck deck, int deckPrice) {
        this.deck = deck;
        this.deckPrice = deckPrice;
    }

    public Long getId() {
        return id;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getDeckPrice() {
        return deckPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setDeckPrice(int deckPrice) {
        this.deckPrice = deckPrice;
    }
}
