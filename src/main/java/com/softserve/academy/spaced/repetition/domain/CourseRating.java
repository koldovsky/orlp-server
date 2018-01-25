package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.utils.dto.EntityInterface;
import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MAX_COURSE_AND_CARD_RATING;
import static com.softserve.academy.spaced.repetition.utils.validators.ValidationConstants.MIN_COURSE_AND_CARD_RATING;

@Entity
@Table(name = "course_rating")
public class CourseRating implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private Long id;

    @Column(name = "account_email", nullable = false)
    private String accountEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "rating", nullable = false)
    @Min(message = "{message.validation.ratingMinValue}", value = MIN_COURSE_AND_CARD_RATING, groups = Request.class)
    @Max(message = "{message.validation.ratingMaxValue}", value = MAX_COURSE_AND_CARD_RATING, groups = Request.class)
    private int rating;

    public CourseRating() {

    }

    public CourseRating(String accountEmail, Course course, int rating) {
        this.accountEmail = accountEmail;
        this.course = course;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
