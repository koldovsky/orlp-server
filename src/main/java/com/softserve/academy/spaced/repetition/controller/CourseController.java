package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.CoursePublic;
import com.softserve.academy.spaced.repetition.DTO.impl.CoursePublicDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/category/{category_id}/courses", method = RequestMethod.GET)
    public List<CoursePublic> getAllCoursesByCategoryId(@PathVariable Long category_id) {
        List<Course> courses = courseService.getAllCoursesByCategoryId(category_id);
        List<CoursePublic> coursesPublic = new ArrayList<>();
        for (Course course : courses) {
            coursesPublic.add(new CoursePublicDTO(course));
        }
        return coursesPublic;
    }

    @RequestMapping(value = "/category/{category_id}/courses/{course_id}", method = RequestMethod.GET)
    public CoursePublic getCourse(@PathVariable Long course_id) {
        CoursePublic coursePublic = new CoursePublicDTO(courseService.getCourse(course_id));
        return coursePublic;
    }

    @RequestMapping(value = "/category/{category_id}/courses", method = RequestMethod.POST)
    public ResponseEntity<CoursePublic> addCourse(@RequestBody Course course, @PathVariable Long category_id) {
        courseService.addCourse(course, category_id);
        return new ResponseEntity<>(new CoursePublicDTO(course), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{user_id}/courses/{course_id}", method = RequestMethod.PUT)
    public void updateCourse(@PathVariable Long course_id, @RequestBody Course course) {
        courseService.updateCourse(course_id, course);
    }

    @RequestMapping(value = "/user/{user_id}/courses/{course_id}", method = RequestMethod.DELETE)
    public void deleteCourse(@PathVariable Long course_id) {
        courseService.deleteCourse(course_id);
    }
}
