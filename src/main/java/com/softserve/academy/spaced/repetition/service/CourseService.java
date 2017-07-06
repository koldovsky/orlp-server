package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCoursesByCategoryId(Long category_id) {
        return courseRepository.getAllCoursesByCategoryId(category_id);
    }

    public Course getCourse(Long course_id) {
        return courseRepository.findOne(course_id);
    }

    public void addCourse(Course course, Long category_id) {
        course.setCategory(new Category(category_id));
        courseRepository.save(course);
    }

    public void updateCourse(Long course_id, Course course) {
        course.setId(course_id);
        courseRepository.save(course);
    }

    public void deleteCourse(Long course_id) {
        courseRepository.delete(course_id);
    }
}
