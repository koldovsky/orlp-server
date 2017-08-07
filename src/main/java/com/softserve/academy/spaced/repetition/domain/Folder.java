package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Folder")
public class Folder implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "folder_id")
    private long id;

    @OneToMany(fetch = FetchType.LAZY)
    private List <Deck> decks;

    public Folder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List <Deck> getDecks() {
        return decks;
    }

    public void setDecks(List <Deck> decks) {
        this.decks = decks;
    }
}
