package com.softserve.academy.spaced.repetition.service.impl;


import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import com.softserve.academy.spaced.repetition.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final int QUANTITY_CATEGORIES_IN_PAGE = 8;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageService imageService;

    @Override
    @Transactional
    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    @Transactional
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id);
        return category;

    }

    @Override
    public List<Category> getTopCategory() {
        List<Category> categories = categoryRepository.findTop8ByOrderById();
        return categories;
    }

    @Override
    @Transactional
    public Category addCategory(String categoryName, String categoryDescription, Image categoryImage) {
        Category category = new Category(categoryName, categoryDescription, categoryImage);
        Long imageId = category.getImage().getId();
        imageService.setImageStatusInUse(imageId);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> getSortedCategories(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_CATEGORIES_IN_PAGE, ascending
                ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return categoryRepository.findAll(request);
    }
}

