package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CourseComment")
public class CourseComment extends Comment {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseComment() {
        super();
    }

    public CourseComment(String commentText, Date commentDate) {
        super(commentText, commentDate);
    }

    public CourseComment(Course course) {
        super();
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
