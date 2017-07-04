package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jarki on 6/28/2017.
 */

@Entity
@Table(name = "Folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "folder_id")
    private long id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Deck> decks;

    public Folder() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }
}
