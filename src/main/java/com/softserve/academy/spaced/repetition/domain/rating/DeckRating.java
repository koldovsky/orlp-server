package com.softserve.academy.spaced.repetition.domain.rating;

import javax.persistence.*;

@Entity
@Table(name = "deck_rating")
public class DeckRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id",nullable = false)
    private int userId;

    @Column(name = "deck_id",nullable = false)
    private int deckId;

    @Column(name = "rating",nullable = false)
    private int rating;

    public DeckRating() {

    }

    public DeckRating(int userId, int deckId, int rating) {
        this.userId = userId;
        this.deckId = deckId;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
