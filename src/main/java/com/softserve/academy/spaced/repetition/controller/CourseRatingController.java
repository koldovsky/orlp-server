package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.dto.RatingDTO;
import com.softserve.academy.spaced.repetition.dto.impl.CourseRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.service.impl.CourseRatingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CourseRatingController {

    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;

    @Autowired
    private CourseRatingServiceImpl courseRatingService;

    @GetMapping("api/course/{courseId}/rating/{id}")
    public ResponseEntity<CourseRatingPublicDTO> getCourseRatingById(@PathVariable Long courseId, @PathVariable Long id) {
        CourseRating courseRating = courseRatingService.getCourseRatingById(id);
        Link selfLink = linkTo(methodOn(CourseRatingController.class).getCourseRatingById(courseRating.getCourse().getId(), courseRating.getId())).withRel("courseRating");
        CourseRatingPublicDTO courseRatingDTO = DTOBuilder.buildDtoForEntity(courseRating, CourseRatingPublicDTO.class, selfLink);
        return new ResponseEntity<>(courseRatingDTO, HttpStatus.OK);
    }

    @PostMapping("/api/private/course/{courseId}")
    public ResponseEntity addCourseRating(@RequestBody RatingDTO ratingDTO, @PathVariable Long courseId) throws NotAuthorisedUserException, UserStatusException {
        if ((ratingDTO.getRating() >= MIN_RATING) && (ratingDTO.getRating() <= MAX_RATING)) {
            courseRatingService.addCourseRating(ratingDTO.getRating(), courseId);
           return new ResponseEntity(HttpStatus.CREATED);
        } else {
            throw new IllegalArgumentException("Rating can't be less than 1 and more than 5");
        }
    }
}
