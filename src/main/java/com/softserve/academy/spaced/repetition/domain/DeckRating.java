package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

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

    @Column(name = "deck_id", nullable = false)
    private long deckId;

    @Column(name = "rating", nullable = false)
    private int rating;

    public DeckRating() {
    }

    public Long getId() {
        return id;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public long getDeckId() {
        return deckId;
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

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

