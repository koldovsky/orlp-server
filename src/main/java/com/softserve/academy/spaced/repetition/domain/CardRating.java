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
@Table(name = "card_rating")
public class CardRating implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private Long id;

    @Column(name = "account_email", length = EMAIL_MAX_SIZE)
    @NotNull
    private String accountEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "rating")
    @NotNull
    @Min(value = MIN_COURSE_AND_CARD_RATING, message = "{message.validation.ratingMinValue}", groups = Request.class)
    @Max(value = MAX_COURSE_AND_CARD_RATING, message = "{message.validation.ratingMaxValue}", groups = Request.class)
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
