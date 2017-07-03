package com.softserve.academy.spaced.repetition.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController {
	@Autowired
    ApplicationContext applicationContext;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(value="/category", method=RequestMethod.GET)
	public List<Category> getCategories(){
		return categoryService.getAllCategory();
	}
	
	@RequestMapping(value="/category/{id}", method=RequestMethod.GET)
	public Category getCategoryById(@PathVariable Long id){
		return categoryService.getCategoryById(id);
	}
	@RequestMapping(value="/topcategory", method=RequestMethod.GET)
	public List<Category> get4Categories(){
		return categoryService.get4Category();
	}
	

}
