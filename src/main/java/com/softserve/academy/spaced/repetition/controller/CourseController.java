package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.CoursePublic;
import com.softserve.academy.spaced.repetition.DTO.impl.CoursePublicDTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/category/{id}/courses", method = RequestMethod.GET)
    public List<CoursePublic> getAllCoursesByCategoryId(@PathVariable Long id) {
        List<Course> courses = courseService.getAllCoursesByCategoryId(id);
        List<CoursePublic> coursesPublic = new ArrayList<>();
        for (Course course : courses) {
            coursesPublic.add(new CoursePublicDTO(course));
        }
        return coursesPublic;
    }

    @RequestMapping(value = "/category/{id}/courses/{id}", method = RequestMethod.GET)
    public CoursePublic getCourse(@PathVariable Long id) {
        CoursePublic coursePublic = new CoursePublicDTO(courseService.getCourse(id));
        return coursePublic;
    }

    @RequestMapping(value = "/category/{category_id}/courses", method = RequestMethod.POST)
    public ResponseEntity<?> addCourse(@RequestBody Course course, @PathVariable Long category_id) {
        course.setCategory(new Category(category_id));
        courseService.addCourse(course);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

//    @RequestMapping(value = "/courses/{id}", method = RequestMethod.DELETE)
//    public void deleteCourse(@PathVariable Long id) {
//        courseService.deleteCourse(id);
//    }
}
