package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.CategoryDTO;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This interface works with the categories.
 */
public interface CategoryService {
    /**
     * Returns all categories that exist.
     *
     * @return list of categories
     */
    List<Category> getAllCategory();

    /**
     * Finds category with the given identifier.
     *
     * @param categoryId must not be {@literal null}.
     * @return category with given id
     */
    Category getCategoryById(Long categoryId);

    /**
     * Finds first 8 categories in DB
     *
     * @return list of categories
     */
    List<Category> getTopCategory();

    /**
     * Adds category with given parameters.
     *
     * @param categoryName        must not be {@literal null}.
     * @param categoryDescription must not be {@literal null}.
     * @param categoryImage       must be less than {app.images.maxSize}.
     * @return added category
     */
    Category addCategory(String categoryName, String categoryDescription, Image categoryImage);

    /**
     * Update category with the given identifier on new.
     *
     * @param category   new category, must not be {@literal null}.
     * @param categoryId must not be {@literal null}.
     * @return updated category
     */
    Category updateCategory(CategoryDTO category, Long categoryId);

    /**
     * Return sorted categories on each page.
     *
     * @param pageNumber zero-based page index.
     * @param sortBy     the properties to sort by, must not be null or empty.
     * @param ascending  the direction of sorting, if true sort by ASC otherwise DESC
     * @return sorted category on each page (by default 8 categories on each page)
     */
    Page<Category> getSortedCategories(int pageNumber, String sortBy, boolean ascending);

    void deleteCategory(Long categoryId);

    List<Category> findAllCategoriesBySearch(String searchString);
}
