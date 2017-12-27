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
import org.mockito.InjectMocks;
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

    @InjectMocks
    private DeckServiceImpl deckService;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @Mock
    private FolderServiceImpl folderServiceImpl;

    final int PAGE_NUMBER = 1;
    final String SORT_BY = "id";

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
