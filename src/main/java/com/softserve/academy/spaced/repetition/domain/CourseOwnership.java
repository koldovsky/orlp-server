package com.softserve.academy.spaced.repetition.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "COURSE")
public class CourseOwnership extends Ownership {
    private Long courseId;

    public CourseOwnership() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
