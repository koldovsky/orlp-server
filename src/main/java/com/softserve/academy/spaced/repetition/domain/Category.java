package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "category")
public class Category extends EntityForOwnership implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    @NotNull
    private String description;

    @OneToOne()
    @JoinColumn(name = "image")
    private Image image;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;

    @OneToMany(mappedBy = "category")
    private List<Deck> decks;

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(String name, String description, Image image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Category(Long id, String name, Image image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Category(Long id, String name, String description, Image image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
