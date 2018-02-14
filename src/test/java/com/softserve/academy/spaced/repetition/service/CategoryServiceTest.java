package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.service.impl.CategoryServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class CategoryServiceTest {

    private final Long CATEGORY_ID = 1L;
    private final Long IMAGE_ID = 1L;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category category;
    private List<Category> categoryList;
    private Image image;

    @Before
    public void setUp() {
        categoryList = new ArrayList<>();
        category = DomainFactory.createCategory(CATEGORY_ID, null, null, image);
        categoryList.add(category);
        image = DomainFactory.createImage(IMAGE_ID, null, null, null, null, false);
    }

    @Test
    public void testGetAllCategory() {
        when(categoryRepository.findAll()).thenReturn(categoryList);

        List<Category> result = categoryService.getAllCategory();
        verify(categoryRepository).findAll();
        assertEquals(categoryList, result);
    }

    @Test
    public void testGetCategoryById() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(category);

        Category result = categoryService.getCategoryById(CATEGORY_ID);
        verify(categoryRepository).findById(CATEGORY_ID);
        assertEquals(category, result);
    }

    @Test
    public void testGetTopCategory() {
        when(categoryRepository.findTop8ByOrderById()).thenReturn(categoryList);

        List<Category> result = categoryService.getTopCategory();
        verify(categoryRepository).findTop8ByOrderById();
        assertEquals(categoryList, result);
    }

    @Test
    public void testAddCategory() {
        doNothing().when(imageService).setImageStatusInUse(IMAGE_ID);
        when(categoryRepository.save(category)).thenReturn(any(Category.class));

        Category result = categoryService.addCategory(null, null, image);
        verify(imageService).setImageStatusInUse(IMAGE_ID);
        verify(categoryRepository).save(any(Category.class));
        assertNull(result);
    }

    @Test
    public void testGetSortedCategories() {
        final int PAGE_NUMBER = 1;
        final boolean PAGE_ASCENDING_ORDER = true;
        final String PAGE_SORT_BY = "field";

        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(null);

        Page<Category> result = categoryService.getSortedCategories(PAGE_NUMBER, PAGE_SORT_BY, PAGE_ASCENDING_ORDER);
        verify(categoryRepository).findAll(any(PageRequest.class));
        assertNull(result);
    }
}
