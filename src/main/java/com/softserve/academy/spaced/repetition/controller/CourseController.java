package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CourseLinkDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CoursePublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CourseTopDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.DeckPublicDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.service.AccessToUrlService;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private AccessToUrlService accessToUrlService;

    @GetMapping(value = "/api/category/{category_id}/courses")
    public ResponseEntity<List<CoursePublicDTO>> getAllCoursesByCategoryId(@PathVariable Long category_id) {
        if (accessToUrlService.hasAccessToCourse(category_id)) {
            List<Course> courseList = courseService.getAllCoursesByCategoryId(category_id);
            Link collectionLink = linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(category_id)).withRel("course");
            List<CoursePublicDTO> courses = DTOBuilder.buildDtoListForCollection(courseList,
                    CoursePublicDTO.class, collectionLink);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/course/top")
    public ResponseEntity<List<CourseTopDTO>> get4Course() {
        List<Course> courseList = courseService.get4Course();
        Link collectionLink = linkTo(methodOn(CourseController.class).get4Course()).withSelfRel();
        List<CourseTopDTO> courses = DTOBuilder.buildDtoListForCollection(courseList,
                CourseTopDTO.class, collectionLink);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }


    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}")
    public ResponseEntity<CourseLinkDTO> getCourseById(@PathVariable Long category_id, @PathVariable Long course_id) {
        if (accessToUrlService.hasAccessToCourse(category_id, course_id)) {
            Course course = courseService.getCourseById(category_id, course_id);
            Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(category_id, course_id)).withRel("course");
            CourseLinkDTO linkDTO = DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink);
            return new ResponseEntity<>(linkDTO, HttpStatus.OK);
        } else {
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/category/{category_id}/courses")
    public ResponseEntity<CoursePublicDTO> addCourse(@RequestBody Course course, @PathVariable Long category_id) {
        courseService.addCourse(course, category_id);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(category_id, course.getId())).withSelfRel();
        CoursePublicDTO coursePublicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
        return new ResponseEntity<>(coursePublicDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/api/user/{user_id}/courses/{course_id}")
    public void updateCourse(@PathVariable Long course_id, @RequestBody Course course) {
        courseService.updateCourse(course_id, course);
    }

    @DeleteMapping(value = "/api/user/{user_id}/courses/{course_id}")
    public void deleteCourse(@PathVariable Long course_id) {
        courseService.deleteCourse(course_id);
    }
}
