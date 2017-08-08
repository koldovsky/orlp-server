package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Category")
public class Category implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "imagebase64", columnDefinition = "LONGTEXT", nullable = false)
    private String imagebase64;

    @OneToMany(mappedBy = "category")
    private List<Course> courses;

    @OneToMany(mappedBy = "category")
    private List<Deck> decks;

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(String name, String description, String imagebase64) {
        this.name = name;
        this.description = description;
        this.imagebase64 = imagebase64;
    }

    public Category(Long id, String name, String imagebase64) {
        this.id = id;
        this.name = name;
        this.imagebase64 = imagebase64;
    }

    public Category(Long id, String name, String description, String imagebase64) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagebase64 = imagebase64;
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

    public String getImagebase64() {
        return imagebase64;
    }

    public void setImagebase64(String imagebase64) {
        this.imagebase64 = imagebase64;
    }

}
