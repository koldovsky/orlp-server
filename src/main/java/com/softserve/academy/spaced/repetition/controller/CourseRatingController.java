package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CardRatingPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CourseRatingPublicDTO;
import com.softserve.academy.spaced.repetition.domain.CourseRating;
import com.softserve.academy.spaced.repetition.exceptions.MoreThanOneTimeRateException;
import com.softserve.academy.spaced.repetition.service.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CourseRatingController {

    @Autowired
    private CourseRatingService courseRatingService;

    @PostMapping("/api/category/course/{courseId}")
    public ResponseEntity<DTO<CourseRating>> addCourseRating(@RequestBody CourseRating courseRating, @PathVariable Long courseId) throws MoreThanOneTimeRateException {

        if (courseRating.getRating() <= 5 && courseRating.getRating() >= 0) {
            courseRatingService.addCourseRating(courseRating,courseId);
            Link selfLink = linkTo(methodOn(CardRatingController.class).getCardRatingById(courseRating.getId())).withSelfRel();
            CourseRatingPublicDTO courseRatingPublicDTO = DTOBuilder.buildDtoForEntity(courseRating, CourseRatingPublicDTO.class, selfLink);
            return new ResponseEntity<>(courseRatingPublicDTO, HttpStatus.CREATED);
        } else
            throw new MoreThanOneTimeRateException();
    }

}
