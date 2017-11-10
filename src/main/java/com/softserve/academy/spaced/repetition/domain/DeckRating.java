package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.dto.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "deck_rating")
public class DeckRating implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private Long id;

    @Column(name = "account_email", nullable = false)
    private String accountEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @Column(name = "rating", nullable = false)
    private int rating;

    public DeckRating() {
    }

    public DeckRating(String accountEmail, Deck deck, int rating) {
        this.accountEmail = accountEmail;
        this.deck = deck;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

