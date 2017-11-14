package com.softserve.academy.spaced.repetition.service;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;

import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class CourseServiceTest {

    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DeckRepository deckRepository;

    private final int PAGE_NUMBER = 1;
    private final String SORT_BY = "name";
    private final boolean ASCENDING = true;

    @Before
    public void setUp() throws Exception {
        courseService = new CourseService();
        courseService.setCourseRepository(courseRepository);
        courseService.setUserService(userService);
        courseService.setUserRepository(userRepository);
        courseService.setImageService(imageService);
        courseService.setImageRepository(imageRepository);
        courseService.setCategoryRepository(categoryRepository);
        courseService.setDeckRepository(deckRepository);
    }

    @Test
    public void testCoursesInPage() {

        assertEquals(SORT_BY + ": ASC", courseService.getPageWithCourses(PAGE_NUMBER, SORT_BY, ASCENDING).getSort().toString());
        assertTrue(courseService.getPageWithCourses(PAGE_NUMBER, SORT_BY, ASCENDING).getTotalPages() == 1);
        assertTrue(courseService.getPageWithCourses(PAGE_NUMBER, SORT_BY, ASCENDING).getNumberOfElements() == 5);
    }

    @Test
    public void testCoursesByCategoryAndByPage() {

        final int CATEGORY_ID = 2;
        assertEquals(SORT_BY + ": ASC", courseService.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, ASCENDING).getSort().toString());
        assertTrue(courseService.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, ASCENDING).getTotalPages() == 1);
        assertTrue(courseService.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, ASCENDING).getNumberOfElements() == 1);
    }
}
