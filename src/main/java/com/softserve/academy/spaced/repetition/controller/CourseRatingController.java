package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardRatingPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CourseRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.exceptions.MoreThanOneTimeRateException;
import com.softserve.academy.spaced.repetition.exceptions.RatingsBadValueException;
import com.softserve.academy.spaced.repetition.exceptions.UserIsNotAuthorizedException;
import com.softserve.academy.spaced.repetition.service.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CourseRatingController {

    @Autowired
    private CourseRatingService courseRatingService;

    @PostMapping("/api/private/course/{courseId}")
    public ResponseEntity<DTO<CourseRating>> addCourseRating(@RequestBody CourseRating courseRating, @PathVariable Long courseId) throws MoreThanOneTimeRateException, RatingsBadValueException, UserIsNotAuthorizedException {

        if (courseRating.getRating() <= 5 && courseRating.getRating() >= 0) {
            courseRatingService.addCourseRating(courseRating,courseId);
            Link selfLink = linkTo(methodOn(CourseRatingController.class).getCourseRatingById(courseRating.getId())).withSelfRel();
            CourseRatingPublicDTO courseRatingPublicDTO = DTOBuilder.buildDtoForEntity(courseRating, CourseRatingPublicDTO.class, selfLink);
            return new ResponseEntity<>(courseRatingPublicDTO, HttpStatus.CREATED);
        } else
            throw new RatingsBadValueException();
    }

    @GetMapping("api/rate/course")
    public ResponseEntity<List<CourseRatingPublicDTO>> getCourseRating() {

        List<CourseRating> courseRatingsList = courseRatingService.getAllCardRating();
        Link collectionLink = linkTo(methodOn(CourseRatingController.class).getCourseRating()).withRel("course");
        List<CourseRatingPublicDTO> courseRatings = DTOBuilder.buildDtoListForCollection(courseRatingsList, CourseRatingPublicDTO.class, collectionLink);
        return new ResponseEntity<>(courseRatings, HttpStatus.OK);
    }

    @GetMapping("api/rate/course/{id}")
    public ResponseEntity<CourseRatingPublicDTO> getCourseRatingById(@PathVariable Long id) {

        CourseRating courseRating = courseRatingService.getCourseRatingById(id);
        Link selfLink = linkTo(methodOn(CourseRatingController.class).getCourseRatingById(courseRating.getId())).withRel("courseRating");
        CourseRatingPublicDTO courseRatingDTO = DTOBuilder.buildDtoForEntity(courseRating, CourseRatingPublicDTO.class, selfLink);
        return new ResponseEntity<>(courseRatingDTO, HttpStatus.OK);
    }

}
