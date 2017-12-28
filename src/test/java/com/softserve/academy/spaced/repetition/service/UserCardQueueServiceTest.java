package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.domain.enums.UserCardQueueStatus;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import com.softserve.academy.spaced.repetition.service.impl.UserCardQueueServiceImpl;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static com.softserve.academy.spaced.repetition.service.impl.AccountServiceImpl.NUMBER_OF_REMEMBERING_LEVELS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class UserCardQueueServiceTest {
    private static final int DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
    private static final Long ACCOUNT_ID = 1L;
    private static final Long CARD_ID = 1L;
    private static final Long DECK_ID = 1L;
    private static final Long USER_ID = 1L;

    @ClassRule
    public static final SpringClassRule SCR = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private UserCardQueueService userCardQueueService;

    @Autowired
    private UserCardQueueRepository userCardQueueRepository;

    @Autowired
    private RememberingLevelRepository rememberingLevelRepository;

    @Mock
    private UserService mockedUserService;

    @Parameter
    public boolean isExisting;

    @Parameter(1)
    public UserCardQueueStatus startingStatus;

    @Parameter(2)
    public UserCardQueueStatus status;

    @Parameter(3)
    public Integer startingOrderNumber;

    @Parameter(4)
    public Integer expectedOrderNumber;

    @Parameter(5)
    public Date startingDateToRepeat;

    @Parameter(6)
    public LearningRegime learningRegime;

    private RememberingLevel startingRememberingLevel;
    private RememberingLevel expectedRememberingLevel;

    @Before
    public void setUp() throws Exception {
        userCardQueueService = new UserCardQueueServiceImpl(userCardQueueRepository, mockedUserService,
                rememberingLevelRepository);
        User mockedUser = createMockedUser(learningRegime);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        startingRememberingLevel = rememberingLevelRepository
                .findRememberingLevelByAccountEqualsAndOrderNumber(mockedUser.getAccount(), startingOrderNumber);
        expectedRememberingLevel = rememberingLevelRepository
                .findRememberingLevelByAccountEqualsAndOrderNumber(mockedUser.getAccount(), expectedOrderNumber);
    }

    @Parameters
    public static Collection<Object[]> data() throws NotAuthorisedUserException {
        Collection<Object[]> params = new ArrayList<>();
        params.add(new Object[]{false, null, UserCardQueueStatus.GOOD, null, 2, new Date(),
        LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{false, null, UserCardQueueStatus.NORMAL, null, 1, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{false, null, UserCardQueueStatus.BAD, null, 1, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, null, UserCardQueueStatus.GOOD, 4, 5, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, null, UserCardQueueStatus.NORMAL, 4, 4, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, null, UserCardQueueStatus.BAD, 4, 3, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, UserCardQueueStatus.BAD, UserCardQueueStatus.GOOD, 4, 5, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, UserCardQueueStatus.BAD, UserCardQueueStatus.NORMAL, 4, 4, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, UserCardQueueStatus.NORMAL, UserCardQueueStatus.BAD, 4, 3, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, null, UserCardQueueStatus.BAD, 1, 1, new Date(),
                LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{true, null, UserCardQueueStatus.GOOD, NUMBER_OF_REMEMBERING_LEVELS,
                NUMBER_OF_REMEMBERING_LEVELS, new Date(), LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION});
        params.add(new Object[]{false, null, UserCardQueueStatus.GOOD, null, null, null,
                LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING});
        params.add(new Object[]{true, UserCardQueueStatus.BAD, UserCardQueueStatus.GOOD, null, null, null,
                LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING});
        params.add(new Object[]{true, UserCardQueueStatus.BAD, UserCardQueueStatus.GOOD, 1, 1, new Date(),
                LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING});
        return params;
    }

    @Test
    public void testUpdateUserCardQueue() throws NotAuthorisedUserException {
        UserCardQueue actualUserCardQueue;
        if (!isExisting) {
            actualUserCardQueue = userCardQueueRepository.findUserCardQueueByUserIdAndCardId(
                    USER_ID, CARD_ID);
            if (actualUserCardQueue != null) {
                userCardQueueRepository.delete(actualUserCardQueue.getId());
            }
        } else {
            buildUserCardQueueAndSave(startingStatus, startingRememberingLevel, startingDateToRepeat);
        }

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status.getStatus());

        actualUserCardQueue = userCardQueueRepository.findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        Date expectedDateToRepeat;
        UserCardQueueStatus expectedStatus;
        if (learningRegime == LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION) {
            expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                    + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
            expectedStatus = startingStatus;
        } else {
            expectedDateToRepeat = startingDateToRepeat;
            expectedStatus = status;
        }
        checkFields(expectedStatus, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    private void buildUserCardQueueAndSave(UserCardQueueStatus status, RememberingLevel rememberingLevel,
                                                    Date dateToRepeat) {
        UserCardQueue userCardQueue = new UserCardQueue();
        userCardQueue.setCardId(CARD_ID);
        userCardQueue.setDeckId(DECK_ID);
        userCardQueue.setUserId(USER_ID);
        userCardQueue.setCardDate(new Date());
        userCardQueue.setStatus(status);
        userCardQueue.setRememberingLevel(rememberingLevel);
        userCardQueue.setDateToRepeat(dateToRepeat);
        userCardQueueRepository.save(userCardQueue);
    }

    private User createMockedUser(LearningRegime learningRegime) {
        Account mockedAccount = new Account();
        mockedAccount.setId(ACCOUNT_ID);
        mockedAccount.setLearningRegime(learningRegime);
        User mockedUser = new User();
        mockedUser.setId(USER_ID);
        mockedUser.setAccount(mockedAccount);
        return mockedUser;
    }

    private void checkFields(UserCardQueueStatus status, UserCardQueue actualUserCardQueue,
                             RememberingLevel rememberingLevel, Date dateToRepeat) {
        assertNotNull("id", actualUserCardQueue.getId());
        assertEquals("cardId", CARD_ID, actualUserCardQueue.getCardId());
        assertEquals("deckId", DECK_ID, actualUserCardQueue.getDeckId());
        assertEquals("accountEmail", USER_ID, actualUserCardQueue.getUserId());
        assertNotNull("cardDate", actualUserCardQueue.getCardDate());
        assertEquals("status", status, actualUserCardQueue.getStatus());
        assertEquals("rememberingLevel", rememberingLevel, actualUserCardQueue.getRememberingLevel());
        assertEquals("dateToRepeat", dateToRepeat, actualUserCardQueue.getDateToRepeat());
    }
}
