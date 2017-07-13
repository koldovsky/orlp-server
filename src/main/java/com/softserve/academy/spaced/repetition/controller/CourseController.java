package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CoursePublicDTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/api/category/{category_id}/courses", method = RequestMethod.GET)
    public ResponseEntity<List<CoursePublicDTO>> getAllCoursesByCategoryId(@PathVariable Long category_id) {
//        List<Course> courses = courseService.getAllCoursesByCategoryId(category_id);
//        List<CoursePublicDTO> coursesPublic = new ArrayList<>();
//        for (Course course : courses) {
//            Link selfLink = linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(course.getId())).withSelfRel();
//            coursesPublic.add(new CoursePublicDTO(course, selfLink));
//        }
//        return coursesPublic;

        try {
            List<Course> courseList = courseService.getAllCoursesByCategoryId(category_id);
            Link collectionLink = linkTo(methodOn(CourseController.class).getAllCoursesByCategoryId(category_id)).withSelfRel();
            List<CoursePublicDTO> courses = DTOBuilder.buildDtoListForCollection(courseList,
                    CoursePublicDTO.class, collectionLink);

            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/api/category/{category_id}/courses/{course_id}", method = RequestMethod.GET)
    public ResponseEntity<CoursePublicDTO> getCourse(@PathVariable Long category_id, @PathVariable Long course_id) {
//        Course course = courseService.getCourse(course_id);
////        course.add(linkTo(CourseController.class).withSelfRel());
//        Link selfLink = linkTo(methodOn(CourseController.class).getCourse(course.getId())).withSelfRel();
//        CoursePublicDTO coursePublic = new CoursePublicDTO(course, selfLink);
//        return coursePublic;
        try {
            Course course = courseService.getCourse(course_id);
            Link selfLink = linkTo(methodOn(CourseController.class).getCourse(category_id,course_id)).withSelfRel();
            CoursePublicDTO publicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
            return new ResponseEntity<>(publicDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/api/category/{category_id}/courses", method = RequestMethod.POST)
    public ResponseEntity<CoursePublicDTO> addCourse(@RequestBody Course course, @PathVariable Long category_id) {
        courseService.addCourse(course, category_id);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourse(category_id, course.getId())).withSelfRel();
        return new ResponseEntity<>(new CoursePublicDTO(course, selfLink), HttpStatus.OK);
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
