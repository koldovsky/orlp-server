package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.impl.DeckServiceImpl;
import com.softserve.academy.spaced.repetition.service.impl.FolderServiceImpl;
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
import static org.mockito.Matchers.eq;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class DeckServiceTest {
    private DeckServiceImpl deckService;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Mock
    private FolderServiceImpl folderServiceImpl;

    final int PAGE_NUMBER = 1;
    final String SORT_BY = "id";

    @Before
    public void setUp() throws Exception {
        deckService = new DeckServiceImpl();
        deckService.setCourseRepository(courseRepository);
        deckService.setDeckRepository(deckRepository);
        deckService.setCardRepository(cardRepository);
        deckService.setCategoryRepository(categoryRepository);
        deckService.setCourseRepository(courseRepository);
        deckService.setUserService(userService);
        deckService.setFolderService(folderServiceImpl);
    }

    @Test
    public void testDeckInPage() {
        assertEquals(SORT_BY + ": ASC", deckService.getPageWithAllAdminDecks(PAGE_NUMBER, SORT_BY, true).getSort().toString());
        assertTrue(deckService.getPageWithAllAdminDecks(PAGE_NUMBER, SORT_BY, true).getTotalPages() == 1);
        assertTrue(deckService.getPageWithAllAdminDecks(PAGE_NUMBER, SORT_BY, true).getNumberOfElements() == 11);
    }

    @Test
    public void testDeckInPageByCategory() {
        final int CATEGORY_ID = 1;
        assertEquals(SORT_BY + ": ASC", deckService.getPageWithDecksByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getSort().toString());
        assertTrue(deckService.getPageWithDecksByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getTotalPages() == 1);
        assertTrue(deckService.getPageWithDecksByCategory(CATEGORY_ID, PAGE_NUMBER, SORT_BY, true).getNumberOfElements() == 4);
    }
}
