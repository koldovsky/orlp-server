package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.*;
import com.softserve.academy.spaced.repetition.controller.dto.simpledto.CourseDTO;
import com.softserve.academy.spaced.repetition.controller.dto.simpledto.PriceDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.service.CourseService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseCommentController.class);

    @Autowired
    private CourseService courseService;

    @Auditable(action = AuditingAction.VIEW_COURSES_VIA_CATEGORY)
    @GetMapping(value = "/api/categories/{categoryId}/courses")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<Page<CoursePriceDTO>> getAllCoursesByCategoryId(@PathVariable Long categoryId,
                                                                          @RequestParam(name = "p", defaultValue = "1")
                                                                                  int pageNumber,
                                                                          @RequestParam(name = "sortBy") String sortBy,
                                                                          @RequestParam(name = "asc") boolean ascending) {
        Page<CoursePriceDTO> coursePriceDTOS = courseService
                .getPageWithCoursesByCategory(categoryId, pageNumber, sortBy, ascending).map(course -> {
                    Link selfLink = linkTo(methodOn(CourseController.class)
                            .getAllCoursesByCategoryId(categoryId, pageNumber, sortBy, ascending)).withSelfRel();
                    return DTOBuilder.buildDtoForEntity(course, CoursePriceDTO.class, selfLink);
                });
        return new ResponseEntity<>(coursePriceDTOS, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_COURSES)
    @GetMapping(value = "/api/courses")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<Page<CoursePriceDTO>> getAllCourses(@RequestParam(name = "p", defaultValue = "1") int pageNumber,
                                                              @RequestParam(name = "sortBy") String sortBy,
                                                              @RequestParam(name = "asc") boolean ascending) {
        Page<CoursePriceDTO> coursePriceDTOS = courseService
                .getPageWithCourses(pageNumber, sortBy, ascending).map(course -> {
                    Link selfLink = linkTo(methodOn(CourseController.class)
                            .getCourseById(course.getId())).withSelfRel();
                    return DTOBuilder.buildDtoForEntity(course, CoursePriceDTO.class, selfLink);
                });
        return new ResponseEntity<>(coursePriceDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/api/courses/orders")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<List<CourseLinkDTO>> getAllCoursesOrderByRating() {
        List<Course> courseList = courseService.getAllOrderedCourses();
        Link collectionLink = linkTo(methodOn(CourseController.class).getAllCoursesOrderByRating()).withRel("courses");
        List<CourseLinkDTO> decks = DTOBuilder
                .buildDtoListForCollection(courseList, CourseLinkDTO.class, collectionLink);
        return new ResponseEntity<>(decks, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_TOP_COURSES)
    @GetMapping("/api/courses/top")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<List<CourseLinkDTO>> getTopCourse() {
        List<Course> courseList = courseService.getTopCourse();
        List<CourseLinkDTO> courses = new ArrayList<>();
        for (Course course : courseList) {
            Link selfLink = linkTo(methodOn(CourseController.class)
                    .getCourseById(course.getId())).withSelfRel();
            courses.add(DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink));
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/api/courses/{courseId}")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<CourseLinkDTO> getCourseById(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(courseId)).withSelfRel();
        CourseLinkDTO linkDTO = DTOBuilder.buildDtoForEntity(course, CourseLinkDTO.class, selfLink);
        return new ResponseEntity<>(linkDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/courses/{courseId}")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<CourseEditDTO> getCourseByIdByAdmin(@PathVariable Long courseId) {
        Course course = courseService.getCourseById(courseId);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseByIdByAdmin(courseId)).withSelfRel();
        CourseEditDTO dto = DTOBuilder.buildDtoForEntity(course, CourseEditDTO.class, selfLink);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_COURSES_ADMIN)
    @GetMapping(value = "/api/admin/courses")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<Page<CourseEditDTO>> getAllCoursesByAdmin(@RequestParam(name = "p", defaultValue = "1") int pageNumber,
                                                                    @RequestParam(name = "sortBy") String sortBy,
                                                                    @RequestParam(name = "asc") boolean ascending) {
        Page<CourseEditDTO> courseEditDTOS = courseService
                .getPageWithCourses(pageNumber, sortBy, ascending).map(course -> {
                    Link selfLink = linkTo(methodOn(CourseController.class)
                            .getCourseByIdByAdmin(course.getId())).withSelfRel();
                    return DTOBuilder.buildDtoForEntity(course, CourseEditDTO.class, selfLink);
                });
        return new ResponseEntity<>(courseEditDTOS, HttpStatus.OK);
    }

    //TODO fix this method
    @Auditable(action = AuditingAction.CREATE_COURSE)
    @PostMapping(value = "/api/courses")
    @PreAuthorize("hasPermission('COURSE','CREATE')")
    public ResponseEntity<CoursePublicDTO> addCourse(@RequestBody Course course, @PathVariable Long categoryId) {
        LOGGER.debug("Adding course to category with id: {}", categoryId);
        courseService.addCourse(course, categoryId);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();
        CoursePublicDTO coursePublicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
        return new ResponseEntity<>(coursePublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_COURSE_ADMIN)
    @PutMapping(value = "/api/admin/courses/{courseId}")
    @PreAuthorize("hasPermission('COURSE','UPDATE')")
    public ResponseEntity<CoursePublicDTO> updateCourse(@PathVariable Long courseId, @Validated(Request.class) @RequestBody CourseDTO courseDTO) {
        LOGGER.debug("Updating course with id: {}", courseId);
        Course course = courseService.updateCourse(courseId, courseDTO);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();
        CoursePublicDTO coursePublicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
        return new ResponseEntity<>(coursePublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_COURSE)
    @PreAuthorize("hasPermission('COURSE','DELETE') &&" +
            "@courseServiceImpl.getCourseById(#courseId).createdBy==principal.id")
    @DeleteMapping(value = "/api/cabinet/global/courses/{courseId}")
    public ResponseEntity deleteGlobalCourse(@PathVariable Long courseId) throws NotAuthorisedUserException {
        LOGGER.debug("Deleting global course with id: {}", courseId);
        courseService.deleteGlobalCourse(courseId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/cabinet/local/courses/{courseId}")
    @PreAuthorize("hasPermission('COURSE','DELETE')")
    public ResponseEntity deleteLocalCourse(@PathVariable Long courseId) throws NotAuthorisedUserException {
        LOGGER.debug("Deleting global course with id: {}", courseId);
        courseService.deleteLocalCourse(courseId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_COURSE_ADMIN)
    @DeleteMapping(value = "/api/admin/courses/{courseId}")
    @PreAuthorize("hasPermission('COURSE','DELETE')")
    public ResponseEntity deleteCourseByAdmin(@PathVariable Long courseId) {
        LOGGER.debug("Deleting course with id: {}", courseId);
        courseService.deleteCourseAndSubscriptions(courseId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Auditable(action = AuditingAction.ADD_COURSE)
    @PostMapping("/api/cabinet/courses/{courseId}")
    @PreAuthorize("hasPermission('COURSE','CREATE')")
    public ResponseEntity<CourseUpdatedDTO> addCourse(@PathVariable Long courseId) throws NotAuthorisedUserException {
        LOGGER.debug("Adding course with id: {}", courseId);
        Course course = courseService.updateListOfCoursesOfTheAuthorizedUser(courseId);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();
        CourseUpdatedDTO courseUpdatedDTO = DTOBuilder.buildDtoForEntity(course, CourseUpdatedDTO.class, selfLink);
        return new ResponseEntity<>(courseUpdatedDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.ADD_COURSE)
    @PutMapping("/api/cabinet/{courseId}/update/access")
    @PreAuthorize("hasPermission('COURSE','UPDATE') && @courseServiceImpl.getCourseById(#courseId).createdBy==principal.id")
    public ResponseEntity<CoursePublicDTO> updateCourseAccess(@PathVariable Long courseId,
                                                              @Validated(Request.class) @RequestBody Course course) {
        LOGGER.debug("Updating course with id: {}", courseId);
        courseService.updateCourseAccess(courseId, course);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();
        CoursePublicDTO coursePublicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
        return new ResponseEntity<>(coursePublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.ADD_COURSE)
    @PreAuthorize("hasPermission('COURSE','CREATE')")
    @PutMapping("/api/categories/courses/{courseId}/decks/{deckId}")
    public ResponseEntity addDeckToCourse(@Validated(Request.class) @PathVariable Long courseId,
                                          @PathVariable Long deckId) {
        LOGGER.debug("Adding deck with id: {} to course with id: {}", deckId, courseId);
        Course course = courseService.addDeckToCourse(courseId, deckId);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();
        CoursePublicDTO coursePublicDTO = DTOBuilder.buildDtoForEntity(course, CoursePublicDTO.class, selfLink);
        return new ResponseEntity<>(coursePublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_DECK_FROM_COURSE)
    @DeleteMapping("/api/categories/courses/{courseId}/decks/{deckId}")
    @PreAuthorize("hasPermission('COURSE','UPDATE') && @courseServiceImpl.getCourseById(#courseId).createdBy==principal.id")
    public ResponseEntity deleteDeckFromCourse(@Validated(Request.class) @PathVariable Long courseId,
                                          @PathVariable Long deckId) {
        LOGGER.debug("Deleting deck with id: {} from course with id: {}", deckId, courseId);
        courseService.deleteDeckFromCourse(courseId, deckId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/private/cabinet/courses")
    @PreAuthorize("hasPermission('COURSE','READ')")
    public ResponseEntity<List<Long>> getIdAllCoursesOfTheCurrentUser() throws NotAuthorisedUserException {
        List<Long> id = courseService.getAllCoursesIdOfTheCurrentUser();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_PRIVATE_COURSE)
    @PostMapping("/api/categories/{categoryId}/courses")
    @PreAuthorize("hasPermission('COURSE','CREATE')")
    public ResponseEntity<CourseUpdatedDTO> createPrivateCourse(@Validated(Request.class) @RequestBody Course privateCourse,
                                                                @PathVariable Long categoryId) throws NotAuthorisedUserException {
        LOGGER.debug("Creating private course in category with id: {}", categoryId);
        Course course = courseService.createPrivateCourse(privateCourse, categoryId);
        Link selfLink = linkTo(methodOn(CourseController.class).getCourseById(course.getId())).withSelfRel();
        CourseUpdatedDTO courseUpdatedDTO = DTOBuilder.buildDtoForEntity(course, CourseUpdatedDTO.class, selfLink);
        return new ResponseEntity<>(courseUpdatedDTO, HttpStatus.OK);
    }

    @PutMapping("/api/courses/{courseId}")
    @PreAuthorize("hasPermission('COURSE','UPDATE')")
    public ResponseEntity updateCoursePrice(@Validated(Request.class) @RequestBody PriceDTO priceDTO,
                                            @PathVariable Long courseId) {
        LOGGER.debug("Updating course price with Integer: {}", priceDTO.getPrice());
        courseService.updateCoursePrice(priceDTO.getPrice(), courseId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
