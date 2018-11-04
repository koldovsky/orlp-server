package com.softserve.academy.spaced.repetition.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "course", indexes = {
        @Index(columnList = "name", name = "course_index"),
        @Index(columnList = "description", name = "course_index")
})
public class Course extends EntityForOwnership implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    @NotNull
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    private Image image;

    @Column(name = "rating")
    private double rating;

    @Column(name = "published")
    private boolean published;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "course_decks", joinColumns = {
            @JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "deck_id")})
    private List <Deck> decks;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseRating> courseRatings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseComment> courseComments;

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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<CourseRating> getCourseRatings() {
        return courseRatings;
    }

    public void setCourseRatings(List<CourseRating> courseRatings) {
        this.courseRatings = courseRatings;
    }

    public List<CourseComment> getCourseComments() {
        return courseComments;
    }

    public void setCourseComments(List<CourseComment> courseComments) {
        this.courseComments = courseComments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (Double.compare(course.rating, rating) != 0) return false;
        if (published != course.published) return false;
        if (id != null ? !id.equals(course.id) : course.id != null) return false;
        if (name != null ? !name.equals(course.name) : course.name != null) return false;
        if (description != null ? !description.equals(course.description) : course.description != null) return false;
        if (image != null ? !image.equals(course.image) : course.image != null) return false;
        if (owner != null ? !owner.equals(course.owner) : course.owner != null) return false;
        if (category != null ? !category.equals(course.category) : course.category != null) return false;
        if (decks != null ? !decks.equals(course.decks) : course.decks != null) return false;
        if (courseRatings != null ? !courseRatings.equals(course.courseRatings) : course.courseRatings != null)
            return false;
        return courseComments != null ? courseComments.equals(course.courseComments) : course.courseComments == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (published ? 1 : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (decks != null ? decks.hashCode() : 0);
        result = 31 * result + (courseRatings != null ? courseRatings.hashCode() : 0);
        result = 31 * result + (courseComments != null ? courseComments.hashCode() : 0);
        return result;
    }
}
