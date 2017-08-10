package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Folder")
public class Folder implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "folder_id")
    private long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "folder_decks", joinColumns = {
            @JoinColumn(name = "folder_id")},
            inverseJoinColumns = {@JoinColumn(name = "deck_id")})
    private Set<Deck> decks;

    public Folder() {}

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Set<Deck> decks) {
        this.decks = decks;
    }
}
