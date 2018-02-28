package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.service.CourseService;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class SearchController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private DeckService deckService;


    @GetMapping(value = "search/{searchString}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getLinksFromSearch(@PathVariable String searchString) {
        List<String> links = new ArrayList<>();
        courseService.findCoursesId(searchString).forEach(deckId -> links.add(linkTo(methodOn(DeckController.class)
                .getDeckById(deckId.longValueExact())).withSelfRel().getHref()));
        deckService.findDecksId(searchString).forEach(deckId -> links.add(linkTo(methodOn(DeckController.class)
                .getDeckById(deckId.longValueExact())).withSelfRel().getHref()));
        return links;
    }
}
