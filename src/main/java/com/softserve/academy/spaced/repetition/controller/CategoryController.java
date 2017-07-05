package com.softserve.academy.spaced.repetition.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public List<Category> getCategories() {
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @RequestMapping(value = "/topcategory", method = RequestMethod.GET)
    public List<Category> get4Categories() {
        return categoryService.get4Category();
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
