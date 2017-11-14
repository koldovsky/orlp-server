package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.CategoryRepository;
import com.softserve.academy.spaced.repetition.repository.CourseRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
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

import java.util.ArrayList;
import java.util.List;

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
    private DeckService deckService;

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
    private FolderService folderService;

    final int PAGE_NUMBER = 1;
    final String SORT_BY = "id";
    final boolean ASCENDING = true;

    @Before
    public void setUp() throws Exception {
        deckService = new DeckService();
        deckService.setCourseRepository(courseRepository);
        deckService.setDeckRepository(deckRepository);
        deckService.setCardRepository(cardRepository);
        deckService.setCategoryRepository(categoryRepository);
        deckService.setCourseRepository(courseRepository);
        deckService.setUserService(userService);
        deckService.setFolderService(folderService);
    }

    @Test
    public void testDeckInPage() {
        assertTrue((deckService.getPageWithAllAdminDecks(PAGE_NUMBER, SORT_BY, ASCENDING).getSort().toString()).equals(SORT_BY +": ASC"));
        assertTrue(deckService.getPageWithAllAdminDecks(PAGE_NUMBER, SORT_BY, ASCENDING).getTotalPages()==1);
        assertTrue(deckService.getPageWithAllAdminDecks(PAGE_NUMBER, SORT_BY, ASCENDING).getNumberOfElements()==11);
    }

    @Test
    public void testDeckInPageByCategory() {
        final int CATEGORY_ID = 1;
        assertTrue((deckService.getPageWithDecksByCategory(CATEGORY_ID,PAGE_NUMBER, SORT_BY, ASCENDING).getSort().toString()).equals(SORT_BY +": ASC"));
        assertTrue(deckService.getPageWithDecksByCategory(CATEGORY_ID,PAGE_NUMBER, SORT_BY, ASCENDING).getTotalPages()==1);
        assertTrue(deckService.getPageWithDecksByCategory(CATEGORY_ID,PAGE_NUMBER, SORT_BY, ASCENDING).getNumberOfElements()==4);
    }
}
