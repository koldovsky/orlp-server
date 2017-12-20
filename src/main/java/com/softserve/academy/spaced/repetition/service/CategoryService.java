package com.softserve.academy.spaced.repetition.service;


import java.util.List;

import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageServiceImpl imageServiceImpl;

    private final int QUANTITY_CATEGORIES_IN_PAGE = 8;

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
        imageServiceImpl.setImageStatusInUse(imageId);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Long id) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    public Page<Category> getSortedCategories(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_CATEGORIES_IN_PAGE, ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return categoryRepository.findAll(request);
    }
}

