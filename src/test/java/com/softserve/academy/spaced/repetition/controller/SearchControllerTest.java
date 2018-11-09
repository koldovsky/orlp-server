package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Image;
import com.softserve.academy.spaced.repetition.service.CategoryService;
import com.softserve.academy.spaced.repetition.service.CourseService;
import com.softserve.academy.spaced.repetition.service.DeckService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private SearchController searchController;

    @Mock
    private CourseService courseService;
    @Mock
    private DeckService deckService;
    @Mock
    private CategoryService categoryService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(searchController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }


    private List<Deck> deckList() {
        List<Deck> decks =  new ArrayList<>();
        Deck deck1 = new Deck();
        deck1.setId(1L);
        deck1.setName("Java deck 1");
        deck1.setDescription("Java deck 1 description");
        deck1.setRating(3.0);
        Deck deck2 = new Deck();
        deck2.setId(2L);
        deck2.setName("Java deck 2");
        deck2.setDescription("Java deck 2 description");
        deck2.setRating(2.9);
        decks.add(deck1);
        decks.add(deck2);
        return decks;
    }

    private List<Course> courseList() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Java course 1");
        course1.setDescription("Java course 1 description");
        course1.setImage(new Image());
        course1.setRating(3.3);
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Java course 2");
        course2.setDescription("Java course 2 description");
        course2.setImage(new Image());
        course2.setRating(4.4);
        courses.add(course1);
        courses.add(course2);
        return courses;
    }

    private List<Category> categoryList() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Java category 1");
        category1.setDescription("Java category 1 description");
        category1.setImage(new Image());
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Java category 2");
        category2.setDescription("Java category 2 description");
        category2.setImage(new Image());
        categories.add(category1);
        categories.add(category2);
        return categories;
    }

    @Test
    public void getResultsFromSearch() throws Exception {
        when(deckService.findAllDecksBySearch("Java")).thenReturn(deckList());
        when(courseService.findAllCoursesBySearch("Java")).thenReturn(courseList());
        when(categoryService.findAllCategoriesBySearch("Java")).thenReturn(categoryList());
        mockMvc.perform(get("/search/{searchString}", "Java")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category.[0].name", is("Java category 1")))
                .andExpect(jsonPath("$.deck.[1].rating", is(2.9)))
                .andExpect(jsonPath("$.course[1].name", is("Java course 2")));
        verify(deckService, times(1)).findAllDecksBySearch("Java");
        verify(courseService, times(1)).findAllCoursesBySearch("Java");
        verify(categoryService, times(1)).findAllCategoriesBySearch("Java");
    }
}
