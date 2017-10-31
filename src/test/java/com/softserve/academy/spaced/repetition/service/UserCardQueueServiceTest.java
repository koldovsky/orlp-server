package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.config.TestDatabaseConfig;
import com.softserve.academy.spaced.repetition.domain.*;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
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

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("testdatabase")
@SpringBootTest
@Import(TestDatabaseConfig.class)
@Sql("/data/TestData.sql")
@Transactional
public class UserCardQueueServiceTest {
    private static final int DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
    private static final Long CARD_ID = 1L;
    private static final Long DECK_ID = 1L;
    private static final String ACCOUNT_EMAIL = "bohdan.dub@gmail.com";

    private UserCardQueueService userCardQueueService;

    @Autowired
    private UserCardQueueRepository userCardQueueRepository;

    @Autowired
    private RememberingLevelRepository rememberingLevelRepository;

    @Mock
    private UserService mockedUserService;

    @Before
    public void setUp() throws Exception {
        userCardQueueService = new UserCardQueueService(userCardQueueRepository, mockedUserService,
                rememberingLevelRepository);
    }

    @Test
    public void testUpdateNonExistentUserCardQueueUsingStatus() throws NotAuthorisedUserException {
        UserCardQueueStatus expectedStatus = UserCardQueueStatus.GOOD;
        User mockedUser = createMockedUser(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);

        UserCardQueue actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(
                ACCOUNT_EMAIL, CARD_ID);
        if (actualUserCardQueue != null) {
            userCardQueueRepository.delete(actualUserCardQueue.getId());
        }

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, expectedStatus);

        actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(ACCOUNT_EMAIL, CARD_ID);
        checkFields(expectedStatus, actualUserCardQueue, null, null);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingStatusWithNullRememberingLevelAndDateToRepeat()
            throws NotAuthorisedUserException {
        UserCardQueueStatus startingStatus = UserCardQueueStatus.BAD;
        UserCardQueueStatus expectedStatus = UserCardQueueStatus.GOOD;
        User mockedUser = createMockedUser(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(startingStatus, null, null);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, expectedStatus);

        checkFields(expectedStatus, actualUserCardQueue, null, null);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingStatusWithNotNullRememberingLevelAndDateToRepeat()
            throws NotAuthorisedUserException {
        UserCardQueueStatus startingStatus = UserCardQueueStatus.BAD;
        UserCardQueueStatus expectedStatus = UserCardQueueStatus.GOOD;
        User mockedUser = createMockedUser(LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        RememberingLevel rememberingLevel = rememberingLevelRepository.findOne(1L);
        Date dateToRepeat = new Date();
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(startingStatus, rememberingLevel, dateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, expectedStatus);

        checkFields(expectedStatus, actualUserCardQueue, rememberingLevel, dateToRepeat);
    }

    @Test
    public void testUpdateNonExistentUserCardQueueUsingCardsPostponingWhenStatusIsGood()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.GOOD;
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(2L);
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);

        UserCardQueue actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(
                ACCOUNT_EMAIL, CARD_ID);
        if (actualUserCardQueue != null) {
            userCardQueueRepository.delete(actualUserCardQueue.getId());
        }

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(ACCOUNT_EMAIL, CARD_ID);
        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateNonExistentUserCardQueueUsingCardsPostponingWhenStatusIsNormal()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.NORMAL;
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(1L);
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);

        UserCardQueue actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(
                ACCOUNT_EMAIL, CARD_ID);
        if (actualUserCardQueue != null) {
            userCardQueueRepository.delete(actualUserCardQueue.getId());
        }

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(ACCOUNT_EMAIL, CARD_ID);
        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateNonExistentUserCardQueueUsingCardsPostponingWhenStatusIsBad()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.BAD;
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(1L);
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);

        UserCardQueue actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(
                ACCOUNT_EMAIL, CARD_ID);
        if (actualUserCardQueue != null) {
            userCardQueueRepository.delete(actualUserCardQueue.getId());
        }

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        actualUserCardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(ACCOUNT_EMAIL, CARD_ID);
        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNullStatusWhenStatusIsGood()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.GOOD;
        RememberingLevel startingRememberingLevel = rememberingLevelRepository.findOne(4L);
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(5L);
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(null, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNullStatusWhenStatusIsGoodAndRememberingLevelIsMaximal()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.GOOD;
        RememberingLevel startingRememberingLevel =
                rememberingLevelRepository.findOne(rememberingLevelRepository.count());
        RememberingLevel expectedRememberingLevel =
                rememberingLevelRepository.findOne(rememberingLevelRepository.count());
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(null, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNullStatusWhenStatusIsNormal()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.NORMAL;
        RememberingLevel startingRememberingLevel = rememberingLevelRepository.findOne(4L);
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(4L);
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(null, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNullStatusWhenStatusIsBad()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.BAD;
        RememberingLevel startingRememberingLevel = rememberingLevelRepository.findOne(4L);
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(3L);
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(null, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNullStatusWhenStatusIsBadAndRememberingLevelIsMinimal()
            throws NotAuthorisedUserException {
        UserCardQueueStatus status = UserCardQueueStatus.BAD;
        RememberingLevel startingRememberingLevel = rememberingLevelRepository.findOne(1L);
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(1L);
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(null, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(null, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNotNullStatusWhenStatusIsGood()
            throws NotAuthorisedUserException {
        UserCardQueueStatus startingStatus = UserCardQueueStatus.BAD;
        UserCardQueueStatus status = UserCardQueueStatus.GOOD;
        RememberingLevel startingRememberingLevel = rememberingLevelRepository.findOne(4L);
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(5L);
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(startingStatus, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(startingStatus, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNotNullStatusWhenStatusIsNormal()
            throws NotAuthorisedUserException {
        UserCardQueueStatus startingStatus = UserCardQueueStatus.BAD;
        UserCardQueueStatus status = UserCardQueueStatus.NORMAL;
        RememberingLevel startingRememberingLevel = rememberingLevelRepository.findOne(4L);
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(4L);
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(startingStatus, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(startingStatus, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    @Test
    public void testUpdateExistingUserCardQueueUsingCardsPostponingWithNotNullStatusWhenStatusIsBad()
            throws NotAuthorisedUserException {
        UserCardQueueStatus startingStatus = UserCardQueueStatus.NORMAL;
        UserCardQueueStatus status = UserCardQueueStatus.BAD;
        RememberingLevel startingRememberingLevel = rememberingLevelRepository.findOne(4L);
        RememberingLevel expectedRememberingLevel = rememberingLevelRepository.findOne(3L);
        Date startingDateToRepeat = new Date();
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);
        UserCardQueue actualUserCardQueue = buildUserCardQueueAndSave(startingStatus, startingRememberingLevel,
                startingDateToRepeat);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, status);

        Date expectedDateToRepeat = new Date(actualUserCardQueue.getCardDate().getTime()
                + expectedRememberingLevel.getNumberOfPostponedDays() * DAY_IN_MILLISECONDS);
        checkFields(startingStatus, actualUserCardQueue, expectedRememberingLevel, expectedDateToRepeat);
    }

    private UserCardQueue buildUserCardQueueAndSave(UserCardQueueStatus status, RememberingLevel rememberingLevel,
                                                    Date dateToRepeat) {
        UserCardQueue userCardQueue = new UserCardQueue();
        userCardQueue.setCardId(CARD_ID);
        userCardQueue.setDeckId(DECK_ID);
        userCardQueue.setAccountEmail(ACCOUNT_EMAIL);
        userCardQueue.setCardDate(new Date());
        userCardQueue.setStatus(status);
        userCardQueue.setRememberingLevel(rememberingLevel);
        userCardQueue.setDateToRepeat(dateToRepeat);
        return userCardQueueRepository.save(userCardQueue);
    }

    private User createMockedUser(LearningRegime learningRegime) {
        Account mockedAccount = new Account();
        mockedAccount.setEmail(ACCOUNT_EMAIL);
        mockedAccount.setLearningRegime(learningRegime);
        User mockedUser = new User();
        mockedUser.setAccount(mockedAccount);
        return mockedUser;
    }

    private void checkFields(UserCardQueueStatus status, UserCardQueue actualUserCardQueue,
                             RememberingLevel rememberingLevel, Date dateToRepeat) {
        assertNotNull("id", actualUserCardQueue.getId());
        assertEquals("cardId", CARD_ID, actualUserCardQueue.getCardId());
        assertEquals("deckId", DECK_ID, actualUserCardQueue.getDeckId());
        assertEquals("accountEmail", ACCOUNT_EMAIL, actualUserCardQueue.getAccountEmail());
        assertNotNull("cardDate", actualUserCardQueue.getCardDate());
        assertEquals("status", status, actualUserCardQueue.getStatus());
        assertEquals("rememberingLevel", rememberingLevel, actualUserCardQueue.getRememberingLevel());
        assertEquals("dateToRepeat", dateToRepeat, actualUserCardQueue.getDateToRepeat());
    }

    @Test
    public void testCountCardsThatNeedRepeating() throws NotAuthorisedUserException {
        User mockedUser = createMockedUser(LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION);
        when(mockedUserService.getAuthorizedUser()).thenReturn(mockedUser);

        long actualNumberOfCards = userCardQueueService.countCardsThatNeedRepeating(DECK_ID);

        long expectedNumberOfCards = userCardQueueRepository
                .countAllByAccountEmailEqualsAndDeckIdEqualsAndDateToRepeatBefore(ACCOUNT_EMAIL, DECK_ID, new Date());
        assertEquals(actualNumberOfCards, expectedNumberOfCards);
    }
}
