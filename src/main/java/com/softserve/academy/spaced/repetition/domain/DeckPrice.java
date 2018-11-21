package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "deck_price")
public class DeckPrice implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deck_price_id")
    private Long id;

    @Column(name = "deck_price")
    private Integer price;

    public DeckPrice() {
    }

    public DeckPrice(int price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
