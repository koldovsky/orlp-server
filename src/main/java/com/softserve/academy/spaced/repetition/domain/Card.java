package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.utils.dto.EntityInterface;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Card")
public class Card implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "question", nullable = false, columnDefinition = "LONGTEXT")
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "LONGTEXT")
    private String answer;

    @Column(name = "rating")
    private double rating;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardRating> cardRatings;

    public Card() {
    }

    public Card(String question, String answer, String title) {
        this.title = title;
        this.question = question;
        this.answer = answer;
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

