package com.softserve.academy.spaced.repetition.controller;


import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CourseLinkDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CoursePublicDTO;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Auditable(action = AuditingAction.VIEW_COURSES_VIA_CATEGORY)
    @GetMapping(value = "/api/category/{category_id}/courses")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#category_id)")
    public ResponseEntity<Page<CourseLinkDTO>> getAllCoursesByCategoryId(@PathVariable Long category_id,
                                                                         @RequestParam(name = "p", defaultValue = "1")
                                                                                 int pageNumber,
                                                                         @RequestParam(name = "sortBy") String sortBy,
                                                                         @RequestParam(name = "asc") boolean ascending) {
        Page<CourseLinkDTO> courseLinkDTOS = courseService
                .getPageWithCoursesByCategory(category_id, pageNumber, sortBy, ascending).map((course) -> {
                    Link selfLink = linkTo(methodOn(CourseController.class)
                            .getAllCoursesByCategoryId(category_id, pageNumber, sortBy, ascending)).withSelfRel();
                    return DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink);
                });
        return new ResponseEntity<>(courseLinkDTOS, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_COURSES)
    @GetMapping(value = "/api/courses")
    public ResponseEntity<Page<CourseLinkDTO>> getAllCourses(@RequestParam(name = "p", defaultValue = "1") int pageNumber,
                                                             @RequestParam(name = "sortBy") String sortBy,
                                                             @RequestParam(name = "asc") boolean ascending) {
        Page<CourseLinkDTO> courseLinkDTOS = courseService
                .getPageWithCourses(pageNumber, sortBy, ascending).map((course) -> {
                    Link selfLink = linkTo(methodOn(CourseController.class)
                            .getCourseById(course.getCategory().getId(), course.getId())).withSelfRel();
                    return DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink);
                });
        return new ResponseEntity<>(courseLinkDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/api/courses/ordered")
    public ResponseEntity<List<CourseLinkDTO>> getAllCoursesOrderByRating() {
        List<Course> courseList = courseService.getAllOrderedCourses();
        Link collectionLink = linkTo(methodOn(CourseController.class).getAllCoursesOrderByRating()).withRel("course");
        List<CourseLinkDTO> decks = DTOBuilder
                .buildDtoListForCollection(courseList, CourseLinkDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_TOP_COURSES)
    @GetMapping("/api/course/top")
    public ResponseEntity<List<CourseLinkDTO>> getTopCourse() {
        List<Course> courseList = courseService.getTopCourse();
        List<CourseLinkDTO> courses = new ArrayList<>();
        for (Course course : courseList) {
            Link selfLink = linkTo(methodOn(CourseController.class)
                    .getCourseById(course.getCategory().getId(), course.getId())).withSelfRel();
            courses.add(DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink));
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/api/category/{category_id}/courses/{course_id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCourse(#category_id, #course_id)")
    public ResponseEntity<CourseLinkDTO> getCourseById(@PathVariable Long category_id, @PathVariable Long course_id) {
        Course course = courseService.getCourseById(category_id, course_id);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(category_id, course_id)).withSelfRel();
        CourseLinkDTO linkDTO = DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_COURSE)
    @PostMapping(value = "/api/category/{category_id}/courses")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#category_id)")
    public ResponseEntity<CoursePublicDTO> addCourse(@RequestBody Course course, @PathVariable Long category_id) {
        courseService.addCourse(course, category_id);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(category_id, course.getId())).withSelfRel();
        CoursePublicDTO coursePublicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
        return new ResponseEntity<>(coursePublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.CREATE_COURSE)
    @PutMapping(value = "/api/user/{user_id}/courses/{course_id}")
    public void updateCourse(@PathVariable Long course_id, @Validated(Request.class) @RequestBody Course course) {
        courseService.updateCourse(course_id, course);
    }

    @Auditable(action = AuditingAction.DELETE_COURSE)
    @DeleteMapping(value = "/api/user/global/courses/{course_id}")
    public void deleteGlobalCourse(@PathVariable Long course_id) throws NotAuthorisedUserException {
        courseService.deleteGlobalCourse(course_id);
    }

    @DeleteMapping(value = "/api/user/local/courses/{course_id}")
    public void deleteLocalCourse(@PathVariable Long course_id) throws NotAuthorisedUserException {
        courseService.deleteLocalCourse(course_id);
    }

    @Auditable(action = AuditingAction.ADD_COURSE)
    @PutMapping("/api/user/courses/{course_id}")
    public ResponseEntity addCourse(@PathVariable Long course_id) throws NotAuthorisedUserException {
        Course course = courseService.updateListOfCoursesOfTheAuthorizedUser(course_id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.ADD_COURSE)
    @PutMapping("/api/user/courses/{course_id}/update/access")
    public ResponseEntity updateCourseAccess(@PathVariable Long course_id,
                                             @Validated(Request.class) @RequestBody Course course) {
        courseService.updateCourseAccess(course_id, course);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.ADD_COURSE)
    @PutMapping("/api/category/courses/{courseId}/decks/{deckId}")
    public ResponseEntity addDeckToCourse(@Validated(Request.class) @PathVariable Long courseId,
                                          @PathVariable Long deckId,
                                          @RequestBody Course course) {
        courseService.addDeckToCourse(courseId, deckId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/api/private/user/courses")
    public ResponseEntity<List<Long>> getIdAllCoursesOfTheCurrentUser() throws NotAuthorisedUserException {
        List<Long> id = courseService.getAllCoursesIdOfTheCurrentUser();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_PRIVATE_COURSE)
    @PostMapping("/api/category/{category_id}/private/user/create/course")
    public ResponseEntity<Course> createPrivateCourse(@Validated(Request.class) @RequestBody Course privateCourse,
                                                      @PathVariable Long category_id) throws NotAuthorisedUserException {
        courseService.createPrivateCourse(privateCourse, category_id);
        return new ResponseEntity<>(privateCourse, HttpStatus.OK);
    }
}
