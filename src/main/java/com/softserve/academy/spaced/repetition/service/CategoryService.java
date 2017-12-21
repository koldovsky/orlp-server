package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();

    Category getCategoryById(Long id);

    List<Category> getTopCategory();

    Category addCategory(String categoryName, String categoryDescription, Image categoryImage);

    Category updateCategory(Category category, Long id);

    Page<Category> getSortedCategories(int pageNumber, String sortBy, boolean ascending);
}
