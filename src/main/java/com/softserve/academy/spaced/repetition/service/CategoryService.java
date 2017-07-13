package com.softserve.academy.spaced.repetition.service;


import java.util.List;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Transactional
    public Category getCategoryById(Long id) {
        Category categories = categoryRepository.findById(id);
        courseRepository.getAllCoursesByCategoryId(id);
        categories.getCourses();
        return categories;

    }

    public List<Category> get4Category() {
        List<Category> categories = categoryRepository.findTop4ByOrderById();
        return categories;
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category, Long id) {
        category.setId(id);
        return categoryRepository.save(category);
    }
}
