package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.SearchDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CategorySearchDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CourseSearchDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.DeckSearchDTO;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import com.softserve.academy.spaced.repetition.service.CourseService;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder.buildDtoListForCollection;


@RestController
public class SearchController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private DeckService deckService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping(value = "search/{searchString}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SearchDTO>> getResultsFromSearch(@PathVariable String searchString) {
        List<SearchDTO> results = new ArrayList<>();
        List<CategorySearchDTO> categoryList = buildDtoListForCollection(categoryService.findAllCategoriesBySearch(searchString), CategorySearchDTO.class);
        List<DeckSearchDTO> deckList = buildDtoListForCollection(deckService.findAllDecksBySearch(searchString), DeckSearchDTO.class);
        List<CourseSearchDTO> courseList = buildDtoListForCollection(courseService.findAllCoursesBySearch(searchString), CourseSearchDTO.class);
        results.addAll(categoryList);
        results.addAll(deckList);
        results.addAll(courseList);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
