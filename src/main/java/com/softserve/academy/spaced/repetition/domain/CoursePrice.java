package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "course_price")
public class CoursePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "price_id")
    private long id;

    @Column(name = "price")
    private int price;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public CoursePrice(){}

    public CoursePrice(int price, Course course){
        this.course = course;
        this.price = price;
    }

    public long getId() { return id; }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursePrice that = (CoursePrice) o;
        return id == that.id &&
                price == that.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price);
    }

    @Override
    public String toString() {
        return "CoursePrice{" +
                "id=" + id +
                ", price=" + price +
                ", course=" + course.getId() +
                '}';
    }
}
