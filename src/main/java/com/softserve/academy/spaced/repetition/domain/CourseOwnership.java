package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "course_ownership")
public class CourseOwnership extends Ownership {

    @Column(name = "course_id")
    private Long courseId;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
