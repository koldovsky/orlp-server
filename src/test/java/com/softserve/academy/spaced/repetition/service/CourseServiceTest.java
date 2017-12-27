package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.impl.CourseServiceImpl;
import com.softserve.academy.spaced.repetition.service.impl.ImageServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class CourseServiceTest {

    private CourseServiceImpl courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ImageServiceImpl imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DeckRepository deckRepository;

    private final int PAGE_NUMBER = 1;
    private final String SORT_BY = "name";

    @Before
    public void setUp() throws Exception {
        courseService = new CourseServiceImpl();
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
        assertEquals(SORT_BY + ": ASC", courseService.getPageWithCourses(PAGE_NUMBER, SORT_BY, true).getSort().toString());
        assertTrue(courseService.getPageWithCourses(PAGE_NUMBER, SORT_BY, true).getTotalPages() == 1);
        assertTrue(courseService.getPageWithCourses(PAGE_NUMBER, SORT_BY, true).getNumberOfElements() == 5);
    }

    @Test
    public void testCoursesByCategoryAndByPage() {

        final int CATEGORY_ID = 2;
        assertEquals(SORT_BY + ": ASC", courseService.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getSort().toString());
        assertTrue(courseService.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getTotalPages() == 1);
        assertTrue(courseService.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getNumberOfElements() == 1);
    }
}
