package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;

@Entity
@Table(name = "card_image")
public class CardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_image_id", nullable = false)
    private Long id;

    @Column(name = "imagebase64", columnDefinition = "LONGTEXT")
    @Basic(fetch = FetchType.LAZY)
    private String image;

    @ManyToOne
    private Card card;

    public CardImage() {
    }

    public CardImage(String image) {
        this.image = image;
    }

    public CardImage(String image, Card card) {
        this.image = image;
        this.card = card;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
