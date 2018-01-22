package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.utils.dto.EntityInterface;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Deck")
public class Deck implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deck_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "synthax")
    private String synthaxToHighlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "rating")
    private double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User deckOwner;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL)
    private List<Card> cards;

    @ManyToMany
    @JoinTable(name = "course_decks", joinColumns = {
            @JoinColumn(name = "deck_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    private List <Course> courses;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL)
    private List<DeckRating> deckRatings;

    @ManyToMany
    @JoinTable(name = "folder_decks", joinColumns = {
            @JoinColumn(name = "deck_id")},
            inverseJoinColumns = {@JoinColumn(name = "folder_id")})
    private Set<Folder> folders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deck", cascade = CascadeType.ALL)
    private List<DeckComment> deckComments;

    public Deck() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<DeckRating> getDeckRatings() {
        return deckRatings;
    }

    public void setDeckRatings(List<DeckRating> deckRatings) {
        this.deckRatings = deckRatings;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }

    public List<DeckComment> getDeckComments() {
        return deckComments;
    }

    public void setDeckComments(List<DeckComment> deckComments) {
        this.deckComments = deckComments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        return id.equals(deck.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getSynthaxToHighlight() {
        return synthaxToHighlight;
    }

    public void setSynthaxToHighlight(String synthaxToHighlight) {
        this.synthaxToHighlight = synthaxToHighlight;
    }
}
