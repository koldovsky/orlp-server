package com.softserve.academy.spaced.repetition.controller;

import java.util.ArrayList;
import java.util.List;

import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import com.softserve.academy.spaced.repetition.domain.HATEOASSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "api/category", method = RequestMethod.GET)
    public List<CategoryPublicDTO> getCategories() {
        List<CategoryPublicDTO> categories = new ArrayList<>();
        List<Category> categoryList = categoryService.getAllCategory();
        for (Category category : categoryList) {
            category.add(linkTo(CategoryController.class).slash(category.getId()).withSelfRel());
            categories.add(new CategoryPublicDTO(category));
        }
        return categories;
    }

    @RequestMapping(value = "api/category/{id}", method = RequestMethod.GET)
    public CategoryPublicDTO getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        category.add(linkTo(CategoryController.class).withSelfRel());
        CategoryPublicDTO publicDTO = new CategoryPublicDTO(category);
        return publicDTO;
    }

    @RequestMapping(value = "api/topcategories", method = RequestMethod.GET)
    public List<CategoryPublicDTO> get4Categories() {
        List<CategoryPublicDTO> categories = new ArrayList<>();
        List<Category> categoryList = categoryService.get4Category();
        for (Category category : categoryList) {
            category.add(linkTo(CategoryController.class).slash(category.getId()).withSelfRel());
            categories.add(new CategoryPublicDTO(category));
        }
        return categories;
    }

    @RequestMapping(value = "api/admin/add/category", method = RequestMethod.POST)
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }

    @RequestMapping(value = "api/admin/add/category/{id}", method = RequestMethod.PUT)
    public void updateCategory(@RequestBody Category category, @PathVariable Long id) {
        categoryService.updateCategory(category, id);
    }

}
