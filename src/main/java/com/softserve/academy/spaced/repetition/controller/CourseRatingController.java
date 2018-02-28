package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CourseRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.service.CourseRatingService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/courses/{courseId}/ratings")
public class CourseRatingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseRatingController.class);

    @Autowired
    private CourseRatingService courseRatingService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasPermission('COURSE_RATING','READ')")
    public CourseRatingPublicDTO getCourseRatingById(@PathVariable Long courseId,
                                                     @PathVariable Long id) {
        LOGGER.debug("View rating with id {}", id);
        CourseRating courseRating = courseRatingService.getCourseRatingById(id);
        return buildDtoForEntity(courseRating, CourseRatingPublicDTO.class,
                linkTo(methodOn(CourseRatingController.class).getCourseRatingById(courseId, courseRating.getId())).withSelfRel());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission('COURSE_RATING','CREATE')")
    public CourseRatingPublicDTO addCourseRating(@Validated(Request.class) @RequestBody CourseRating courseRating,
                                                 @PathVariable Long courseId)
            throws NotAuthorisedUserException, UserStatusException {
        LOGGER.debug("Adding rating to course with id: {}", courseId);
        courseRating = courseRatingService.addCourseRating(courseRating.getRating(), courseId);
        return buildDtoForEntity(courseRating, CourseRatingPublicDTO.class,
                linkTo(methodOn(CourseRatingController.class).getCourseRatingById(courseId, courseRating.getId())).withSelfRel());

    }
}
