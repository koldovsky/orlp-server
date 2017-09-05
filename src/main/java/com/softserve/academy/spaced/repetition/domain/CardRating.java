package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "card_rating")
public class CardRating implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private long id;

    @Column(name = "account_email", nullable = false)
    private String accountEmail;

    @Column(name = "deck_id", nullable = false)
    private long deckId;

    @Column(name = "card_id", nullable = false)
    private long cardId;

    @Column(name = "rating", nullable = false)
    private int rating;

    public CardRating() {

    }

    public CardRating(Long id) {
        this.id = id;
    }

    public CardRating(int rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
