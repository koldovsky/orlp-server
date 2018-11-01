package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "deck", indexes = {
        @Index(columnList = "name", name = "deck_index"),
        @Index(columnList = "description", name = "deck_index")
})
public class Deck extends EntityForOwnership implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deck_id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    @NotNull
    private String description;

    @Column(name = "syntax")
    private String syntaxToHighlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "rating")
    private double rating;

    @Column(name = "hidden")
    private boolean hidden;

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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
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

    public String getSyntaxToHighlight() {
        return syntaxToHighlight;
    }

    public void setSyntaxToHighlight(String syntaxToHighlight) {
        this.syntaxToHighlight = syntaxToHighlight;
    }
}
