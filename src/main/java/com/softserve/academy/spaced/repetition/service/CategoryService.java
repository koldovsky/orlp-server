package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageService imageService;

    @Transactional
    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Transactional
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id);
        return category;

    }

    public List<Category> getTopCategory() {
        List<Category> categories = categoryRepository.findTop8ByOrderById();
        return categories;
    }

    @Transactional
    public Category addCategory(String categoryName, String categoryDescription, Image categoryImage) {
        Category category = new Category(categoryName, categoryDescription, categoryImage);
        Long imageId = category.getImage().getId();
        imageService.setImageStatusInUse(imageId);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Long id) {
        category.setId(id);
        return categoryRepository.save(category);
    }
}

