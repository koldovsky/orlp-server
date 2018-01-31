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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserCardQueueServiceTest {

    private final long USER_ID = 1L;
    private final long DECK_ID = 1L;
    private final long CARD_ID = 1L;
    private final long USER_CARD_QUEUE_ID = 1L;
    private final Date USER_CARD_QUEUE_CARD_DATE = new Date();
    private final int REMEMBERING_LEVEL_ORDER_NUMBER = 5;
    private final String USER_CARD_QUEUE_STATUS_GOOD = "GOOD";
    private final LearningRegime LEARNING_REGIME_BAD_NORMAL_GOOD = LearningRegime.BAD_NORMAL_GOOD_STATUS_DEPENDING;
    private final LearningRegime LEARNING_REGIME_CARDS_POSTPONING = LearningRegime.CARDS_POSTPONING_USING_SPACED_REPETITION;
    @InjectMocks
    private UserCardQueueServiceImpl userCardQueueService;
    @Mock
    private UserCardQueueRepository userCardQueueRepository;
    @Mock
    private RememberingLevelRepository rememberingLevelRepository;
    @Mock
    private UserService userService;
    private Account account;
    private UserCardQueue userCardQueue;
    private RememberingLevel rememberingLevel;

    @Before
    public void setUp() throws NotAuthorisedUserException {
        final Long ACCOUNT_ID = 1L;
        final Long REMEMBERING_LEVEL_ID = 1L;
        final Integer REMEMBERING_LEVEL_NUMBER_OF_POSTPONED_DAYS = 1;

        account = DomainFactory.createAccount(ACCOUNT_ID, null, null, null, null, false, null, null,
                LEARNING_REGIME_CARDS_POSTPONING, null, null);
        final User user = DomainFactory.createUser(USER_ID, account, null, null, null);
        rememberingLevel = DomainFactory.createRememberingLevel(REMEMBERING_LEVEL_ID, REMEMBERING_LEVEL_ORDER_NUMBER,
                null, REMEMBERING_LEVEL_NUMBER_OF_POSTPONED_DAYS, account);
        userCardQueue = DomainFactory.createUserCardQueue(USER_CARD_QUEUE_ID, USER_ID, CARD_ID, DECK_ID, null,
                USER_CARD_QUEUE_CARD_DATE, null, rememberingLevel);

        when(userService.getAuthorizedUser()).thenReturn(user);
        when(userCardQueueRepository.findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID)).thenReturn(userCardQueue);
        when(userCardQueueRepository.save(userCardQueue)).thenReturn(userCardQueue);
        when(rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account, 1))
                .thenReturn(rememberingLevel);
    }

    @Test
    public void testUpdateUserCardQueueIfLearningRegimeIsBadNormalGood() throws NotAuthorisedUserException {
        account.setLearningRegime(LEARNING_REGIME_BAD_NORMAL_GOOD);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS_GOOD);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueRepository).findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        verify(userCardQueueRepository).save(userCardQueue);

        account.setLearningRegime(LEARNING_REGIME_CARDS_POSTPONING);
    }

    @Test
    public void testUpdateUserCardQueueIfLearningRegimeIsCardsPostponingAndUserStatusBad() throws NotAuthorisedUserException {
        final String USER_CARD_QUEUE_STATUS_BAD = "BAD";

        when(rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER - 1)).thenReturn(rememberingLevel);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS_BAD);
        verify(userService, times(2)).getAuthorizedUser();
        verify(userCardQueueRepository).findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account, 1);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER - 1);
        verify(userCardQueueRepository).save(userCardQueue);
    }

    @Test
    public void testUpdateUserCardQueueIfLearningRegimeIsCardsPostponingAndUserStatusGood() throws NotAuthorisedUserException {
        when(rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER + 1)).thenReturn(rememberingLevel);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS_GOOD);
        verify(userService, times(2)).getAuthorizedUser();
        verify(userCardQueueRepository).findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account, 1);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER + 1);
        verify(userCardQueueRepository).save(userCardQueue);
    }

    @Test
    public void testUpdateUserCardQueueIfLearningRegimeIsCardsPostponingAndUserStatusNormal() throws NotAuthorisedUserException {
        final String USER_CARD_QUEUE_STATUS_NORMAL = "NORMAL";

        when(rememberingLevelRepository.findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER)).thenReturn(rememberingLevel);

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS_NORMAL);
        verify(userService, times(2)).getAuthorizedUser();
        verify(userCardQueueRepository).findUserCardQueueByUserIdAndCardId(USER_ID, CARD_ID);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account, 1);
        verify(rememberingLevelRepository).findRememberingLevelByAccountEqualsAndOrderNumber(account,
                REMEMBERING_LEVEL_ORDER_NUMBER);
        verify(userCardQueueRepository).save(userCardQueue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserCardQueueIfStatusIsNotValid() throws NotAuthorisedUserException {
        final String INCORRECT_USER_CARD_QUEUE_STATUS = "INCORRECT";

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, INCORRECT_USER_CARD_QUEUE_STATUS);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testUpdateUserCardQueueByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userCardQueueService.updateUserCardQueue(DECK_ID, CARD_ID, USER_CARD_QUEUE_STATUS_GOOD);
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
        final Long COUNT_OF_CARDS_THAT_NEED_REPEATING = 1L;

        when(userCardQueueRepository.countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(eq(USER_ID),
                eq(DECK_ID), any(Date.class))).thenReturn(COUNT_OF_CARDS_THAT_NEED_REPEATING);

        Long result = userCardQueueService.countCardsThatNeedRepeating(DECK_ID);
        verify(userService).getAuthorizedUser();
        verify(userCardQueueRepository).countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(eq(USER_ID),
                eq(DECK_ID), any(Date.class));
        assertEquals(COUNT_OF_CARDS_THAT_NEED_REPEATING, result);
    }

    @Test(expected = NotAuthorisedUserException.class)
    public void testCountCardsThatNeedRepeatingByNotAuthorisedUser() throws NotAuthorisedUserException {
        when(userService.getAuthorizedUser()).thenThrow(new NotAuthorisedUserException());

        userCardQueueService.countCardsThatNeedRepeating(DECK_ID);
        verify(userService).getAuthorizedUser();
    }
}
