package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.DTO.EntityInterface;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Course")
public class Course implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "imagebase64", columnDefinition = "LONGTEXT")
    private String imagebase64;

    @Column(name = "rating")
    private double rating;

    @Column(name = "numb_of_users_ratings")
    private long numbOfUsersRatings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "course_decks", joinColumns = {
            @JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "deck_id")})
    private List <Deck> decks;

    public Course() {
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

    public List <Deck> getDecks() {
        return decks;
    }

    public void setDecks(List <Deck> decks) {
        this.decks = decks;
    }

    public String getImagebase64() {
        return imagebase64;
    }

    public void setImagebase64(String imagebase64) {
        this.imagebase64 = imagebase64;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getNumbOfUsersRatings() {
        return numbOfUsersRatings;
    }

    public void setNumbOfUsersRatings(long numbOfUsersRatings) {
        this.numbOfUsersRatings = numbOfUsersRatings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;

        if (Double.compare(course.rating, rating) != 0) return false;
        if (id != null ? !id.equals(course.id) : course.id != null) return false;
        if (name != null ? !name.equals(course.name) : course.name != null) return false;
        if (description != null ? !description.equals(course.description) : course.description != null) return false;
        if (imagebase64 != null ? !imagebase64.equals(course.imagebase64) : course.imagebase64 != null) return false;
        return category != null ? category.equals(course.category) : course.category == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imagebase64 != null ? imagebase64.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
