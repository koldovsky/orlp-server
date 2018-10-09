package com.softserve.academy.spaced.repetition.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.spaced.repetition.controller.dto.simpleDTO.CategoryDTO;
import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerTest {

    private static final long CATEGORY_ID = 1L;

    private MockMvc mockMvc;

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void testGetAllCategories() throws Exception {
        when(categoryService.getAllCategory()).thenReturn(createCategories());
        mockMvc.perform(get("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description", Matchers.is("Part 1")))
                .andExpect(jsonPath("$.[0].image", Matchers.containsString("/api/service/image/8")))
                .andExpect(jsonPath("$.[0].createdBy", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].name", Matchers.is("Java interview #1")))
                .andExpect(jsonPath("$.[1].description", Matchers.is("Part 2")))
                .andExpect(jsonPath("$.[1].image", Matchers.containsString("/api/service/image/9")))
                .andExpect(jsonPath("$.[1].createdBy", Matchers.is(1)))
                .andExpect(jsonPath("$.[1].name", Matchers.is("C++ interview #2")));
        verify(categoryService, times(1)).getAllCategory();


    }

    @Test
    public void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(createCategory());
        mockMvc.perform(get("/api/categories/{id}", CATEGORY_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", Matchers.is("Part 1")))
                .andExpect(jsonPath("$.image", Matchers.containsString("/api/service/image/8")))
                .andExpect(jsonPath("$.createdBy", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Java interview #1")));
        verify(categoryService, times(1)).getCategoryById(CATEGORY_ID);
    }

    @Test
    public void getTopCategories() throws Exception {
        when(categoryService.getTopCategory()).thenReturn(createCategories());
        mockMvc.perform(get("/api/categories/top")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description", Matchers.is("Part 1")))
                .andExpect(jsonPath("$.[0].image", Matchers.containsString("/api/service/image/8")))
                .andExpect(jsonPath("$.[0].name", Matchers.is("Java interview #1")))
                .andExpect(jsonPath("$.[1].description", Matchers.is("Part 2")))
                .andExpect(jsonPath("$.[1].image", Matchers.containsString("/api/service/image/9")))
                .andExpect(jsonPath("$.[1].name", Matchers.is("C++ interview #2")));
        verify(categoryService, times(1)).getTopCategory();
    }

    @Test
    public void testGetTopSortedCategories() throws Exception {
        when(categoryService.getSortedCategories(1, "id", true))
                .thenReturn(createPageCategory());
        mockMvc.perform(get("/api/sortedCategoriesByPage/top?p=1&sortBy=id&asc=true")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].description", Matchers.is("Part 1")))
                .andExpect(jsonPath("$.content.[0].image", Matchers.containsString("/api/service/image/8")))
                .andExpect(jsonPath("$.content.[0].name", Matchers.is("Java interview #1")))
                .andExpect(jsonPath("$.content.[1].description", Matchers.is("Part 2")))
                .andExpect(jsonPath("$.content.[1].image", Matchers.containsString("/api/service/image/9")))
                .andExpect(jsonPath("$.content.[1].name", Matchers.is("C++ interview #2")));
        verify(categoryService, times(1)).getSortedCategories(1, "id", true);

    }

    @Test
    public void testAddCategory() throws Exception {
        Category category = createCategory();
        when(categoryService.addCategory(eq(category.getName()), eq(category.getDescription()), any(Image.class)))
                .thenReturn(category);
        mockMvc.perform(post("/api/admin/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", Matchers.is("Part 1")))
                .andExpect(jsonPath("$.name", Matchers.is("Java interview #1")));
        verify(categoryService, times(1)).addCategory(eq(category.getName()),
                eq(category.getDescription()), any(Image.class));
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void testUpdateCategory() throws Exception {
        when(categoryService.updateCategory(any(CategoryDTO.class), eq(1L))).thenReturn(createCategory());
        mockMvc.perform(put("/api/categories/{id}", CATEGORY_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createCategory())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", Matchers.is("Part 1")))
                .andExpect(jsonPath("$.name", Matchers.is("Java interview #1")))
                .andExpect(jsonPath("$.image", Matchers.containsString("/api/service/image/8")));
        verify(categoryService, times(1)).updateCategory(any(CategoryDTO.class), eq(1L));
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    public void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);
        mockMvc.perform(delete("/api/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(categoryService, times(1)).deleteCategory(1L);
        verifyZeroInteractions(categoryService);
    }

    private Page<Category> createPageCategory() {
        List<Category> categories = createCategories();
        return new PageImpl<>(categories);
    }

    private List<Category> createCategories() {
        Category category = new Category();
        category.setId(1L);
        category.setDescription("Part 1");
        category.setName("Java interview #1");
        category.setCreatedBy(1L);
        Image image = new Image();
        image.setId(8L);
        category.setImage(image);

        Category category1 = new Category();
        category1.setId(2L);
        category1.setDescription("Part 2");
        category1.setName("C++ interview #2");
        category1.setCreatedBy(1L);
        Image image1 = new Image();
        image1.setId(9L);
        category1.setImage(image1);

        List<Category> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category1);
        return categories;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setDescription("Part 1");
        category.setName("Java interview #1");
        category.setCreatedBy(1L);
        Image image = new Image();
        image.setId(8L);
        image.setIsImageUsed(false);
        category.setImage(image);
        return category;
    }

}