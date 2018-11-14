package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

import com.softserve.academy.spaced.repetition.domain.CoursePrice;
import com.softserve.academy.spaced.repetition.domain.Image;

public class CourseDTO {
    private String name;
    private String description;
    private Image image;
    private CoursePrice coursePrice;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public CoursePrice getCoursePrice() {
        return coursePrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setCoursePrice(CoursePrice coursePrice) {
        this.coursePrice = coursePrice;
    }
}
