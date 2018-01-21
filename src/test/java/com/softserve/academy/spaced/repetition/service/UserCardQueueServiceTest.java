package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import com.softserve.academy.spaced.repetition.service.impl.UserCardQueueServiceImpl;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserCardQueueServiceTest {

    @InjectMocks
    private UserCardQueueServiceImpl userCardQueueService;

    @Mock
    private UserCardQueueRepository userCardQueueRepository;

    @Mock
    private RememberingLevelRepository rememberingLevelRepository;

    @Mock
    private UserService userService;

    private User user;
    private Account account;
    private UserCardQueue userCardQueue;
    private RememberingLevel rememberingLevel;
    private Date date;

    private final long USER_ID = 1L;
    private final long ACCOUNT_ID = 1L;
    private final long DECK_ID = 1L;
    private final long CARD_ID = 1L;
    private final long USER_CARD_QUEUE_ID = 1L;
    private final long REMEMBERING_LEVEL_ID = 1L;
    private final int REMEMBERING_LEVEL_NUMBER_ORDER = 1;

    private final String USER_CARD_QUEUE_STATUS = "GOOD";
    private final String INCORRECT_USER_CARD_QUEUE_STATUS = "INCORRECT";

    private final LearningRegime LEARNING_REGIME = LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING;

    private User createUser(long userId, Account account) {
        User user = new User();
        user.setId(userId);
        user.setAccount(account);
        return user;
    }

    private Account createAccount(long accountId, LearningRegime learningRegime) {
        Account account = new Account();
        account.setId(accountId);
        account.setLearningRegime(learningRegime);
        return account;
    }

    private UserCardQueue createUserCardQueue(long userCardQueueId, long userId, long cardId) {
        UserCardQueue userCardQueue = new UserCardQueue();
        userCardQueue.setId(userCardQueueId);
        userCardQueue.setUserId(userId);
        userCardQueue.setCardId(cardId);
        return userCardQueue;
    }

    private RememberingLevel createRememberingLevel(long rememberingLevelId, int numberOrder, Account account) {
        RememberingLevel rememberingLevel = new RememberingLevel();
        rememberingLevel.setId(rememberingLevelId);
        rememberingLevel.setOrderNumber(numberOrder);
        rememberingLevel.setAccount(account);
        return rememberingLevel;
    }

    private Date createDate() {
        return new Date();
    }

    @Before
    public void setUp() {
        account = createAccount(ACCOUNT_ID, LEARNING_REGIME);
        user = createUser(USER_ID, account);
        userCardQueue = createUserCardQueue(USER_CARD_QUEUE_ID, USER_ID, CARD_ID);
        rememberingLevel = createRememberingLevel(REMEMBERING_LEVEL_ID, REMEMBERING_LEVEL_NUMBER_ORDER, account);

        date = createDate();
    }

    @Test
    public void testUpdateUserCardQueue() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(userCardQueueRepository.findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID)).thenReturn(userCardQueue);
        when(userCardQueueRepository.save(userCardQueue)).thenReturn(userCardQueue);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueRepository).findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        verify(userCardQueueRepository).save(userCardQueue);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testUpdateUserCardQueueByUnauthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS);
        verify(userService).getAuthorizedUser();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserCardQueuePassIncorrectUserCardQueueStatus() throws NotAuthorisedUserException {
        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, INCORRECT_USER_CARD_QUEUE_STATUS);
    }

    @Test
    public void testApplyCardsPostponingLearningRegime() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(user.getAccount(),
                REMEMBERING_LEVEL_NUMBER_ORDER)).thenReturn(rememberingLevel);


    }

    @Test
    public void testGetUserCardQueueById() {
        when(userCardQueueRepository.findOne(USER_CARD_QUEUE_ID)).thenReturn(userCardQueue);

        userCardQueueService.getUserCardQueueById(USER_CARD_QUEUE_ID);
        verify(userCardQueueRepository).findOne(USER_CARD_QUEUE_ID);
    }

    @Test
    public void testCountCardsThatNeedRepeating() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenReturn(user);
        when(userCardQueueRepository.countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(user.getId(),
                DECK_ID, date)).thenReturn(1L);

        userCardQueueService.countCardsThatNeedRepeating(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueRepository).countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(user.getId(),
                DECK_ID, date);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCountCardsThatNeedRepeatingForUnauthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        userCardQueueService.countCardsThatNeedRepeating(DECK_ID);
        verify(userService).getAuthorizedUser();
    }

}
