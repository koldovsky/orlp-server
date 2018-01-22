package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.domain.enums.LearningRegime;
import com.softserve.academy.spaced.repetition.repository.RememberingLevelRepository;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import com.softserve.academy.spaced.repetition.service.impl.UserCardQueueServiceImpl;
import com.softserve.academy.spaced.repetition.util.DomainFactory;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
    private final Date USER_CARD_QUEUE_CARD_DATE = new Date();
    private final long REMEMBERING_LEVEL_ID = 1L;
    private final int REMEMBERING_LEVEL_ORDER_NUMBER = 1;
    private final Integer REMEMBERING_LEVEL_NUMBER_OF_POSTPONED_DAYS = 1;

    private final String USER_CARD_QUEUE_STATUS = "GOOD";
    private final String INCORRECT_USER_CARD_QUEUE_STATUS = "INCORRECT";

    private final LearningRegime LEARNING_REGIME_BAD_NORMAL_GOOD = LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING;
    private final LearningRegime LEARNING_REGIME_CARDS_POSTPONING = LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION;

    @Before
    public void setUp() throws NotAuthorisedUserException {
        account = DomainFactory.createAccount(ACCOUNT_ID, null, null, null, null, false, null, null,
                LEARNING_REGIME_BAD_NORMAL_GOOD, null, null);
        user = DomainFactory.createUser(USER_ID, account, null, null, null);
        rememberingLevel = DomainFactory.createRememberingLevel(REMEMBERING_LEVEL_ID, REMEMBERING_LEVEL_ORDER_NUMBER,
                null, REMEMBERING_LEVEL_NUMBER_OF_POSTPONED_DAYS, account);
        userCardQueue = DomainFactory.createUserCardQueue(USER_CARD_QUEUE_ID, USER_ID, CARD_ID, DECK_ID, null,
                USER_CARD_QUEUE_CARD_DATE, null, rememberingLevel);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(userCardQueueRepository.findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID)).thenReturn(userCardQueue);
        when(userCardQueueRepository.save(userCardQueue)).thenReturn(userCardQueue);
    }

    @Test
    public void testUpdateUserCardQueueIfLearningRegimeIsBadNormalGood() throws NotAuthorisedUserException {
        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueRepository).findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        verify(userCardQueueRepository).save(userCardQueue);
    }

    @Test
    public void testUpdateUserCardQueueIfLearningRegimeIsCardsPostponing() throws NotAuthorisedUserException {
        account.setLearningRegime(LEARNING_REGIME_CARDS_POSTPONING);

        when(rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account, 1))
                .thenReturn(rememberingLevel);
        when(rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER + 1)).thenReturn(rememberingLevel);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueRepository).findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        verify(userCardQueueRepository).save(userCardQueue);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account, 1);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER + 1);

        account.setLearningRegime(LEARNING_REGIME_BAD_NORMAL_GOOD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserCardQueueIfStatusIsNotValid() throws NotAuthorisedUserException {
        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, INCORRECT_USER_CARD_QUEUE_STATUS);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testUpdateUserCardQueueByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS);
        verify(userService).getAuthorizedUser();
    }

    @Test
    public void testGetUserCardQueueById() {
        when(userCardQueueRepository.findOne(USER_CARD_QUEUE_ID)).thenReturn(userCardQueue);

        UserCardQueue result = userCardQueueService.getUserCardQueueById(USER_CARD_QUEUE_ID);
        verify(userCardQueueRepository).findOne(USER_CARD_QUEUE_ID);
        assertEquals(userCardQueue, result);
    }

    @Test
    public void testCountCardsThatNeedRepeating() throws NotAuthorisedUserException {
        Long countOfCardsThatNeedRepeating = 1L;

        when(userCardQueueRepository.countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(eq(USER_ID),
                eq(DECK_ID), any(Date.class))).thenReturn(countOfCardsThatNeedRepeating);

        Long result = userCardQueueService.countCardsThatNeedRepeating(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueRepository).countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(eq(USER_ID),
                eq(DECK_ID), any(Date.class));
        assertEquals(countOfCardsThatNeedRepeating, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCountCardsThatNeedRepeatingByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(NotAuthorisedUserException.class);

        userCardQueueService.countCardsThatNeedRepeating(DECK_ID);
        verify(userService).getAuthorizedUser();
    }
}
