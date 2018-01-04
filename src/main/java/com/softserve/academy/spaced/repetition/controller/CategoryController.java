package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.utils.dto.Request;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.CategoryDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.CategoryLinkDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.CategoryPublicDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.CategoryTopDTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
        List<CategoryLinkDTO> categories = DTOBuilder
                .buildDtoListForCollection(categoryList, CategoryLinkDTO.class, collectionLink);
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
        List<CategoryTopDTO> categories = DTOBuilder
                .buildDtoListForCollection(categoryList, CategoryTopDTO.class, collectionLink);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/api/sortedCategoriesByPage/top")
    public ResponseEntity<Page<CategoryTopDTO>> getTopSortedCategories(@RequestParam(name = "p") int pageNumber,
                                                                       @RequestParam(name = "sortBy") String sortBy,
                                                                       @RequestParam(name = "asc") boolean ascending) {
        Page<CategoryTopDTO> sortedCategoriesDTOS = categoryService
                .getSortedCategories(pageNumber, sortBy, ascending).map((category) -> {
                    Link selfLink = linkTo(methodOn(CategoryController.class)
                            .getCategoryById(category.getId())).withRel("category");
                    return DTOBuilder.buildDtoForEntity(category, CategoryTopDTO.class, selfLink);
                });
        return new ResponseEntity<>(sortedCategoriesDTOS, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_CATEGORY)
    @PostMapping("/api/admin/add/category")
    public ResponseEntity<CategoryPublicDTO> addCategory(@Validated(Request.class) @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService
                .addCategory(categoryDTO.getName(), categoryDTO.getDescription(), categoryDTO.getImage());
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
        CategoryPublicDTO categoryPublicDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
        return new ResponseEntity<>(categoryPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_CATEGORY)
    @PutMapping("/api/admin/add/category/{id}")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCategory(#id)")
    public ResponseEntity<CategoryPublicDTO> updateCategory(@Validated(Request.class) @RequestBody Category category,
                                                            @PathVariable Long id) {
        category = categoryService.updateCategory(category, id);
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
        CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }
}
