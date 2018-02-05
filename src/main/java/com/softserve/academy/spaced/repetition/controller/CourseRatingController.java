package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.RatingDTO;
import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CourseRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.service.CourseRatingService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoForEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/courses/{courseId}/ratings")
public class CourseRatingController {

    @Autowired
    private CourseRatingService courseRatingService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseRatingPublicDTO> getCourseRatingById(@PathVariable Long id) {
        CourseRating courseRating = courseRatingService.getCourseRatingById(id);
        if (courseRating == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Link selfLink = linkTo(methodOn(CourseRatingController.class)
                .getCourseRatingById(courseRating.getId())).withSelfRel();
        CourseRatingPublicDTO courseRatingDTO =
                buildDtoForEntity(courseRating, CourseRatingPublicDTO.class, selfLink);
        return new ResponseEntity<>(courseRatingDTO, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseRatingPublicDTO addCourseRating(@Validated(Request.class) @RequestBody CourseRating courseRating,
                                                 @PathVariable Long courseId)
            throws NotAuthorisedUserException, UserStatusException {
        courseRatingService.addCourseRating(courseRating.getRating(), courseId);
        return buildDtoForEntity(courseRating, CourseRatingPublicDTO.class,
                linkTo(methodOn(CourseRatingController.class)
                        .getCourseRatingById(courseRating.getId())).withSelfRel());

    }
}
