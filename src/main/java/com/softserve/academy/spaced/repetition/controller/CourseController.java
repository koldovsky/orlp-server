package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/category/{id}/courses", method = RequestMethod.GET)
    public List<Course> getAllCoursesByCategoryId(@PathVariable Long id) {
        return courseService.getAllCoursesByCategoryId(id);
    }

    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @RequestMapping(value = "/courses/{id}", method = RequestMethod.GET)
    public Course getCourse(@PathVariable Long id) {
        return courseService.getCourse(id);
    }

    @RequestMapping(value = "/courses", method = RequestMethod.POST)
    public void addCourse(@RequestBody Course course) {
        courseService.addCourse(course);
    }

    @RequestMapping(value = "/courses/{id}", method = RequestMethod.PUT)
    public void updateCourse(@PathVariable Long id, @RequestBody Course course) {
        courseService.updateCourse(id, course);
    }

    @RequestMapping(value = "/courses/{id}", method = RequestMethod.DELETE)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
