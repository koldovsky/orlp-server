package com.softserve.academy.spaced.repetition.controller;

import java.util.List;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryLinkDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryTopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryPublicDTO>> getAllCategories() {
            List<Category> categoryList = categoryService.getAllCategory();
            Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("category");
                List<CategoryPublicDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,
                    CategoryPublicDTO.class, collectionLink);
            return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/api/category/{id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#id)")
    public ResponseEntity<CategoryLinkDTO> getCategoryById(@PathVariable Long id) {
            Category category = categoryService.getCategoryById(id);
            Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withRel("category");
            CategoryLinkDTO publicDTO = DTOBuilder.buildDtoForEntity(category, CategoryLinkDTO.class, selfLink);
            return new ResponseEntity<>(publicDTO, HttpStatus.OK);
    }

    @GetMapping("/api/category/top")
    public ResponseEntity<List<CategoryTopDTO>> getTopCategories() {
            List<Category> categoryList = categoryService.getTopCategory();
            Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel();
            List<CategoryTopDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,
                    CategoryTopDTO.class, collectionLink);
            return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/api/admin/add/category")
    public ResponseEntity<CategoryPublicDTO> addCategory(@RequestBody Category category) {
            category = categoryService.addCategory(category);
            Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
            CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
            return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
    }

    @PutMapping("/api/admin/add/category/{id}")
    public ResponseEntity<CategoryPublicDTO> updateCategory(@RequestBody Category category, @PathVariable Long id) {
            category = categoryService.updateCategory(category, id);
            Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
            CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }
}
