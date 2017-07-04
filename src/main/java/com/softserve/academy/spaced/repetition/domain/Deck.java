package com.softserve.academy.spaced.repetition.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jarki on 6/28/2017.
 */

@Entity
@Table(name = "Deck")
public class Deck implements DeckNameDescriptionPublic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deck_id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User deckOwner;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Card> cards;

    public Deck() {
    }

    public Deck(String name, String description, Category category, User deckOwner, List<Card> cards) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.deckOwner = deckOwner;
        this.cards = cards;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getDeckOwner() {
        return deckOwner;
    }

    public void setDeckOwner(User deckOwner) {
        this.deckOwner = deckOwner;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
