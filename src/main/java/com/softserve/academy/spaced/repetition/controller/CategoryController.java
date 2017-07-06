package com.softserve.academy.spaced.repetition.controller;

import java.util.ArrayList;
import java.util.List;

import com.softserve.academy.spaced.repetition.DTO.CategoryPublic;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private CategoryService categoryService;

//    @RequestMapping(value = "/category", method = RequestMethod.GET)
//    public List<Category> getCategories() {
//        return categoryService.getAllCategory();
//    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public List<CategoryPublic> getCategories() {
        List<CategoryPublic> categories = new ArrayList<>();
        List<Category> categoryList = categoryService.getAllCategory();

        for ( Category category : categoryList) {
            category.add(linkTo(methodOn(CategoryController.class).getCategoryById(category.getId())).withSelfRel());
            categories.add(new CategoryPublicDTO(category));
        }
        return categories;
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public Category getCategoryById(@PathVariable Long id) {
        CategoryPublicDTO publicDTO = new CategoryPublicDTO(categoryService.getCategoryById(id));
        return publicDTO;
    }

    @RequestMapping(value = "/topcategories", method = RequestMethod.GET)
    public List<CategoryPublic> get4Categories() {
        List<CategoryPublic> categories = new ArrayList<>();
        List<Category> categoryList = categoryService.get4Category();
        for ( Category category:categoryList) {
            categories.add(new CategoryPublicDTO(category));
        }
        return categories;
    }

    @RequestMapping(value = "/admin/add/category/", method = RequestMethod.POST)
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }

    @RequestMapping(value = "/admin/add/category/{id}", method = RequestMethod.PUT)
    public void addCategory(@RequestBody Category category, @PathVariable Long id) {
        categoryService.updateCategory(category);
    }

}
