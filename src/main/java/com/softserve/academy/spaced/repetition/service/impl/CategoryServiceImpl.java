package com.softserve.academy.spaced.repetition.service.impl;


import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.CategoryDTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import com.softserve.academy.spaced.repetition.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final int QUANTITY_CATEGORIES_IN_PAGE = 8;
    private final String ALL_CATEGORIES_CACHE_NAME = "allCategories";
    private final String TOP_CATEGORIES_CACHE_NAME = "topCategories";

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageService imageService;


    @Override
    @Cacheable(ALL_CATEGORIES_CACHE_NAME)
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Cacheable(TOP_CATEGORIES_CACHE_NAME)
    public List<Category> getTopCategory() {
        return categoryRepository.findTop8ByOrderById();
    }

    @Override
    @Transactional
    @CacheEvict(value = {ALL_CATEGORIES_CACHE_NAME, TOP_CATEGORIES_CACHE_NAME}, allEntries = true)
    public Category addCategory(String categoryName, String categoryDescription, Image categoryImage) {
        Category category = new Category(categoryName, categoryDescription, categoryImage);
        Long imageId = category.getImage().getId();
        imageService.setImageStatusInUse(imageId);
        return categoryRepository.save(category);
    }

    @CacheEvict(value = {ALL_CATEGORIES_CACHE_NAME, TOP_CATEGORIES_CACHE_NAME}, allEntries = true)
    @Transactional
    public Category updateCategory(CategoryDTO categoryDTO, Long id) {
        Category category = categoryRepository.findById(id);
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setImage(categoryDTO.getImage());
        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> getSortedCategories(int pageNumber, String sortBy, boolean ascending) {
        PageRequest request = new PageRequest(pageNumber - 1, QUANTITY_CATEGORIES_IN_PAGE, ascending
                ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return categoryRepository.findAll(request);
    }

    @Override
    @CacheEvict(cacheNames = {ALL_CATEGORIES_CACHE_NAME, TOP_CATEGORIES_CACHE_NAME}, allEntries = true)
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        if (category.getCourses().isEmpty()) {
            categoryRepository.delete(categoryId);
        }
    }

    @Override
    public List<Category> findAllCategoriesBySearch(String searchString) {
        return categoryRepository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(searchString, searchString);
    }
}

