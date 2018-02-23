package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "card")
public class Card extends EntityForOwnership implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_id")
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "question", columnDefinition = "LONGTEXT")
    @NotNull
    private String question;

    @Column(name = "answer", columnDefinition = "LONGTEXT")
    @NotNull
    private String answer;

    @Column(name = "rating")
    private double rating;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardRating> cardRatings;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardImage> cardImages;

    public Card() {
    }

    public Card(String title, String question, String answer) {
        this.title = title;
        this.question = question;
        this.answer = answer;
    }

    public Card(String title, String question, String answer, Deck deck, List<CardImage> cardImages) {
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.deck = deck;
        this.cardImages = cardImages;
    }

    public Card(Long id, String question, String answer, String title) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<CardRating> getCardRatings() {
        return cardRatings;
    }

    public void setCardRatings(List<CardRating> cardRatings) {
        this.cardRatings = cardRatings;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CardImage> getCardImages() {
        return cardImages;
    }

    public void setCardImages(List<CardImage> cardImages) {
        this.cardImages = cardImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Card card = (Card) o;

        return id.equals(card.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

