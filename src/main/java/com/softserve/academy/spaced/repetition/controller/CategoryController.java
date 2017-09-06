package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryLinkDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryTopDTO;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Auditable(action = AuditingAction.VIEW_ALL_CATEGORIES)
    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryLinkDTO>> getAllCategories() {
        List<Category> categoryList = categoryService.getAllCategory();
        Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("category");
        List<CategoryLinkDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,
                CategoryLinkDTO.class, collectionLink);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_CATEGORY)
    @GetMapping("/api/category/{id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#id)")
    public ResponseEntity<CategoryLinkDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withRel("category");
        CategoryLinkDTO publicDTO = DTOBuilder.buildDtoForEntity(category, CategoryLinkDTO.class, selfLink);
        return new ResponseEntity<>(publicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.VIEW_TOP_CATEGORIES)
    @GetMapping("/api/category/top")
    public ResponseEntity<List<CategoryTopDTO>> getTopCategories() {
        List<Category> categoryList = categoryService.getTopCategory();
        Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel();
        List<CategoryTopDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,
                CategoryTopDTO.class, collectionLink);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_CATEGORY)
    @PostMapping("/api/admin/add/category")
    public ResponseEntity<CategoryPublicDTO> addCategory(@RequestBody Category category) {
        category = categoryService.addCategory(category);
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
        CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
        return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_CATEGORY)
    @PutMapping("/api/admin/add/category/{id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#id)")
    public ResponseEntity<CategoryPublicDTO> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        category = categoryService.updateCategory(category, id);
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
        CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }
}
