package com.softserve.academy.spaced.repetition.service;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;

import com.softserve.academy.spaced.repetition.repository.*;
import com.softserve.academy.spaced.repetition.service.impl.CourseServiceImpl;
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

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class CourseServiceImplTest {

    private CourseServiceImpl courseServiceImpl;

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

    @Before
    public void setUp() throws Exception {
        courseServiceImpl = new CourseServiceImpl();
        courseServiceImpl.setCourseRepository(courseRepository);
        courseServiceImpl.setUserService(userService);
        courseServiceImpl.setUserRepository(userRepository);
        courseServiceImpl.setImageService(imageService);
        courseServiceImpl.setImageRepository(imageRepository);
        courseServiceImpl.setCategoryRepository(categoryRepository);
        courseServiceImpl.setDeckRepository(deckRepository);
    }

    @Test
    public void testCoursesInPage() {
        assertEquals(SORT_BY + ": ASC", courseServiceImpl.getPageWithCourses(PAGE_NUMBER, SORT_BY, true).getSort().toString());
        assertTrue(courseServiceImpl.getPageWithCourses(PAGE_NUMBER, SORT_BY, true).getTotalPages() == 1);
        assertTrue(courseServiceImpl.getPageWithCourses(PAGE_NUMBER, SORT_BY, true).getNumberOfElements() == 5);
    }

    @Test
    public void testCoursesByCategoryAndByPage() {

        final int CATEGORY_ID = 2;
        assertEquals(SORT_BY + ": ASC", courseServiceImpl.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getSort().toString());
        assertTrue(courseServiceImpl.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getTotalPages() == 1);
        assertTrue(courseServiceImpl.getPageWithCoursesByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getNumberOfElements() == 1);
    }
}
