package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "course_price")
public class CoursePrice extends BasePrice implements EntityInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "price_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    public CoursePrice() {
    }

    public CoursePrice(Integer price, Course course) {
        this.course = course;
        setPrice(price);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
