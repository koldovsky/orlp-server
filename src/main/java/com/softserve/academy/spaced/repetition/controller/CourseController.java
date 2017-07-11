package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.CoursePublicDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/api/category/{category_id}/courses", method = RequestMethod.GET)
    public List<CoursePublicDTO> getAllCoursesByCategoryId(@PathVariable Long category_id) {
        List<Course> courses = courseService.getAllCoursesByCategoryId(category_id);
        List<CoursePublicDTO> coursesPublic = new ArrayList<>();
        for (Course course : courses) {
            course.add(linkTo(CourseController.class).slash(course.getId()).withSelfRel());
            coursesPublic.add(new CoursePublicDTO(course));
        }
        return coursesPublic;
    }

    @RequestMapping(value = "/api/category/{category_id}/courses/{course_id}", method = RequestMethod.GET)
    public CoursePublicDTO getCourse(@PathVariable Long course_id) {
        Course course = courseService.getCourse(course_id);
        course.add(linkTo(CourseController.class).withSelfRel());
        CoursePublicDTO coursePublic = new CoursePublicDTO(course);
        return coursePublic;
    }

    @RequestMapping(value = "/api/category/{category_id}/courses", method = RequestMethod.POST)
    public ResponseEntity<CoursePublicDTO> addCourse(@RequestBody Course course, @PathVariable Long category_id) {
        courseService.addCourse(course, category_id);
        return new ResponseEntity<>(new CoursePublicDTO(course), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user/{user_id}/courses/{course_id}", method = RequestMethod.PUT)
    public void updateCourse(@PathVariable Long course_id, @RequestBody Course course) {
        courseService.updateCourse(course_id, course);
    }

    @RequestMapping(value = "/api/user/{user_id}/courses/{course_id}", method = RequestMethod.DELETE)
    public void deleteCourse(@PathVariable Long course_id) {
        courseService.deleteCourse(course_id);
    }
}
