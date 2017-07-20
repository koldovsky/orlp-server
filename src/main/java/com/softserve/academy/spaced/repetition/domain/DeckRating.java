package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "deck_rating")
public class DeckRating implements EntityInterface{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private long id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "deck_id", nullable = false)
    private int deckId;

    @Column(name = "rating", nullable = false)
    private int rating;

    public DeckRating() {
    }

    public DeckRating(Long id) {
        this.id=id;
    }

    public DeckRating(int userId, int deckId, int rating) {
        this.userId = userId;
        this.deckId = deckId;
        this.rating = rating;
    }


    public Long getId() {
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
