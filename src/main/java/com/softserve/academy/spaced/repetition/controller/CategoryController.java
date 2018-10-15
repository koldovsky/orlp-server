package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.annotations.Request;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.CategoryDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CategoryLinkDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CategoryPublicDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CategoryTopDTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/categories")
    @PreAuthorize("hasPermission('CATEGORY','READ')")
    public ResponseEntity<List<CategoryLinkDTO>> getAllCategories() {
        List<Category> categoryList = categoryService.getAllCategory();
        Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("category");
        List<CategoryLinkDTO> categories = DTOBuilder
                .buildDtoListForCollection(categoryList, CategoryLinkDTO.class, collectionLink);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/api/categories/{id}")
    @PreAuthorize("hasPermission('CATEGORY','READ')")
    public ResponseEntity<CategoryLinkDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withRel("category");
        CategoryLinkDTO publicDTO = DTOBuilder.buildDtoForEntity(category, CategoryLinkDTO.class, selfLink);
        return new ResponseEntity<>(publicDTO, HttpStatus.OK);
    }

    @GetMapping("/api/categories/top")
    @PreAuthorize("hasPermission('CATEGORY','READ')")
    public ResponseEntity<List<CategoryTopDTO>> getTopCategories() {
        List<Category> categoryList = categoryService.getTopCategory();
        Link collectionLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withSelfRel();
        List<CategoryTopDTO> categories = DTOBuilder
                .buildDtoListForCollection(categoryList, CategoryTopDTO.class, collectionLink);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/api/sortedCategoriesByPage/top")
    @PreAuthorize("hasPermission('CATEGORY','READ')")
    public ResponseEntity<Page<CategoryTopDTO>> getTopSortedCategories(@RequestParam(name = "p") int pageNumber,
                                                                       @RequestParam(name = "sortBy") String sortBy,
                                                                       @RequestParam(name = "asc") boolean ascending) {
        Page<CategoryTopDTO> sortedCategoriesDTOS = categoryService
                .getSortedCategories(pageNumber, sortBy, ascending).map(category -> {
                    Link selfLink = linkTo(methodOn(CategoryController.class)
                            .getCategoryById(category.getId())).withRel("categories");
                    return DTOBuilder.buildDtoForEntity(category, CategoryTopDTO.class, selfLink);
                });
        return new ResponseEntity<>(sortedCategoriesDTOS, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.CREATE_CATEGORY)
    @PostMapping("/api/admin/categories")
    @PreAuthorize("hasPermission('CATEGORY','CREATE')")
    public ResponseEntity<CategoryPublicDTO> addCategory(@Validated(Request.class) @RequestBody CategoryDTO categoryDTO) {
        LOGGER.debug("Adding category by admin");
        Category category = categoryService
                .addCategory(categoryDTO.getName(), categoryDTO.getDescription(), categoryDTO.getImage());
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
        CategoryPublicDTO categoryPublicDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
        return new ResponseEntity<>(categoryPublicDTO, HttpStatus.CREATED);
    }

    @Auditable(action = AuditingAction.EDIT_CATEGORY)
    @PutMapping("/api/categories/{id}")
    @PreAuthorize("hasPermission('CATEGORY','UPDATE')")
    public ResponseEntity<CategoryPublicDTO> updateCategory(@Validated(Request.class) @RequestBody CategoryDTO categoryDTO,
                                                            @PathVariable Long id) {
        LOGGER.debug("Updating category with id: {}", id);
        Category category = categoryService.updateCategory(categoryDTO, id);
        Link selfLink = linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel();
        CategoryPublicDTO categoryPublicDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class, selfLink);
        return new ResponseEntity<>(categoryPublicDTO, HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.DELETE_CATEGORY)
    @DeleteMapping(value = "/api/categories/{id}")
    @PreAuthorize("hasPermission('CATEGORY','DELETE') && " +
            "@categoryServiceImpl.getCategoryById(#id).createdBy==principal.id")
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        LOGGER.debug("Deleting category with id: {}", id);
        categoryService.deleteCategory(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
