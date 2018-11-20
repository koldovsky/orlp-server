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

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "deck_id")
//    private Deck deck;

    @Column(name = "deck_price")
    private Integer price;

    public DeckPrice() {
    }

    public DeckPrice(Deck deck, int price) {
//        this.deck = deck;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

//    public Deck getDeck() {
//        return deck;
//    }

    public int getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public void setDeck(Deck deck) {
//        this.deck = deck;
//    }

    public void setPrice(int price) {
        this.price = price;
    }
}
