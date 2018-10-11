package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.handler.ExceptionHandlerController;
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

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

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

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(searchController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    private Set<BigInteger> createLinks() {
        Set<BigInteger> links = new HashSet<>();
        links.add(new BigInteger("1"));
        links.add(new BigInteger("10"));
        return links;
    }

    @Test
    public void getLinksFromSearch() throws Exception {
        when(deckService.findDecksId("Java")).thenReturn(createLinks());
        when(courseService.findCoursesId("Java")).thenReturn(createLinks());
        mockMvc.perform(get("/search/{searchString}", "Java")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1]", is("http://localhost/api/decks/10")))
                .andExpect(jsonPath("$.[2]", is("http://localhost/api/courses/1")));
        verify(deckService, times(1)).findDecksId("Java");
        verify(courseService, times(1)).findCoursesId("Java");
    }
}
