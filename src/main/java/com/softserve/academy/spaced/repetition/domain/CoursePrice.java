package com.softserve.academy.spaced.repetition.domain;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.EntityInterface;

import javax.persistence.*;

@Entity
@Table(name = "course_price")
public class CoursePrice extends EntityForOwnership{

    public CoursePrice(){};

    public CoursePrice(int price, Course course){
        this.course = course;
        this.price = price;
    }

    @Column(name = "price")
    private int price;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
