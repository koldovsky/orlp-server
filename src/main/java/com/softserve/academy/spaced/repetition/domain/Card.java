package com.softserve.academy.spaced.repetition.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "Card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_id")
    private long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    public Card() {
    }

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
