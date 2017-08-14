package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CourseLinkDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CoursePublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CourseTopDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/api/category/{category_id}/courses")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#category_id)")
    public ResponseEntity <List <CourseLinkDTO>> getAllCoursesByCategoryId(@PathVariable Long category_id) {
        List <Course> courseList = courseService.getAllCoursesByCategoryId(category_id);
        Link collectionLink = linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(category_id)).withSelfRel();
        List <CourseLinkDTO> courses = DTOBuilder.buildDtoListForCollection(courseList, CourseLinkDTO.class, collectionLink);
        return new ResponseEntity <>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/api/courses")
    public ResponseEntity <List <CourseLinkDTO>> getAllCourses() {
        List <Course> courseList = courseService.getAllCourses();
        List <CourseLinkDTO> courses = new ArrayList <>();
        for (Course course : courseList) {
            Link selfLink = linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(course.getCategory().getId())).withSelfRel();
            courses.add(DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink));
        }
        return new ResponseEntity <>(courses, HttpStatus.OK);
    }

    @GetMapping("/api/course/top")
    public ResponseEntity <List <CourseTopDTO>> get4Course() {
        List <Course> courseList = courseService.get4Course();
        List <CourseTopDTO> courses = new ArrayList <>();
        for (Course course : courseList) {
            Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getCategory().getId(), course.getId())).withSelfRel();
            courses.add(DTOBuilder.buildDtoForEntity(course, CourseTopDTO.class, selfLink));
        }
        return new ResponseEntity <>(courses, HttpStatus.OK);
    }


    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCourse(#category_id, #course_id)")
    public ResponseEntity <CourseLinkDTO> getCourseById(@PathVariable Long category_id, @PathVariable Long course_id) {
        Course course = courseService.getCourseById(category_id, course_id);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(category_id, course_id)).withSelfRel();
        CourseLinkDTO linkDTO = DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink);
        return new ResponseEntity <>(linkDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/api/category/{category_id}/courses")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#category_id)")
    public ResponseEntity <CoursePublicDTO> addCourse(@RequestBody Course course, @PathVariable Long category_id) {
        courseService.addCourse(course, category_id);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(category_id, course.getId())).withSelfRel();
        CoursePublicDTO coursePublicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
        return new ResponseEntity <>(coursePublicDTO, HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/user/{user_id}/courses/{course_id}")
    public void updateCourse(@PathVariable Long course_id, @RequestBody Course course) {
        courseService.updateCourse(course_id, course);
    }

    @DeleteMapping(value = "/api/user/{user_id}/courses/{course_id}")
    public void deleteCourse(@PathVariable Long course_id) {
        courseService.deleteCourse(course_id);
    }


    @PostMapping("/api/user/courses/{course_id}")
    public ResponseEntity addCourse(@RequestBody Long course_id) {
        Course course = courseService.updateListOfCoursesOfTheAuthorizedUser(course_id);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping("/api/private/user/courses")
    public ResponseEntity<List<Long>> getIdAllCoursesOfTheCurrentUser() {
        List<Long> id = courseService.getAllCoursesIdOfTheCurrentUser();
        return new ResponseEntity<List<Long>>(id, HttpStatus.OK);
    }
}
