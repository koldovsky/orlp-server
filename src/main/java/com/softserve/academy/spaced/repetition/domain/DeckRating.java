package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MAX_COURSE_AND_CARD_RATING;
import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MIN_COURSE_AND_CARD_RATING;
import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.EMAIL_MAX_SIZE;

@Entity
@Table(name = "deck_rating")
public class DeckRating implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private Long id;

    @Column(name = "account_email", length = EMAIL_MAX_SIZE)
    @NotNull
    private String accountEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @Column(name = "rating")
    @NotNull
    @Min(value = MIN_COURSE_AND_CARD_RATING, message = "{message.validation.ratingMinValue}", groups = Request.class)
    @Max(value = MAX_COURSE_AND_CARD_RATING, message = "{message.validation.ratingMaxValue}", groups = Request.class)
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

