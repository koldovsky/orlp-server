package com.softserve.academy.spaced.repetition.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public Category getCategoryById(Long id) {
        Category categories = categoryRepository.findById(id);
        return categories;

    }

    public List<Category> get4Category() {
        List<Category> categories = categoryRepository.findTop4ByOrderById();
        return categories;
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public void updateCategory(Category category, Long id) {
        category.setId(id);
        categoryRepository.save(category);
    }
}
