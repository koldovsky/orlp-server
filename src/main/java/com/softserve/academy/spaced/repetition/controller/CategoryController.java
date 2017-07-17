package com.softserve.academy.spaced.repetition.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryLinkDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryTopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryPublicDTO>> getAllCategories() {
        try {
            List<Category> categoryList = categoryService.getAllCategory();
            Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("category");
            List<CategoryPublicDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,
                    CategoryPublicDTO.class, collectionLink);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/category/{id}")
    public ResponseEntity<List<CategoryLinkDTO>> getCategoryById(@PathVariable Long id) {
        try {
            Link selfLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("category");
            List<CategoryLinkDTO> publicDTO = DTOBuilder.buildDtoListForCollection(Collections.singletonList(categoryService.getCategoryById(id)), CategoryLinkDTO.class, selfLink);
            return new ResponseEntity<>(publicDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/category/top")
    public ResponseEntity<List<CategoryTopDTO>> get4Categories() {
        try {
            List<Category> categoryList = categoryService.get4Category();
            Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel();
            List<CategoryTopDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,
                    CategoryTopDTO.class, collectionLink);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/admin/add/category")
    public ResponseEntity<CategoryPublicDTO> addCategory(@RequestBody Category category) {
        try {
            category = categoryService.addCategory(category);
            Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
            CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
            return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/admin/add/category/{id}")
    public ResponseEntity<CategoryPublicDTO> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        try {
            category = categoryService.updateCategory(category, id);
            Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
            CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
