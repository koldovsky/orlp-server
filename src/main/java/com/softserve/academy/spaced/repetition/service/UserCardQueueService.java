package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.UserCardQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserCardQueueService {

    @Autowired
    private UserCardQueueRepository userCardQueueRepository;

    @Autowired
    private UserService userService;

    public void addUserCardQueue(UserCardQueue userCardQueue, long cardId, long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        UserCardQueue cardQueue = userCardQueueRepository.findUserCardQueueByAccountEmailAndCardId(email, cardId);

        if (cardQueue != null) {
            userCardQueue.setId(cardQueue.getId());
        }
        userCardQueue.setAccountEmail(email);
        userCardQueue.setCardId(cardId);
        userCardQueue.setDeckId(deckId);
        userCardQueue.setCardDate(new Date());
        userCardQueueRepository.save(userCardQueue);
    }

    public UserCardQueue getUserCardQueueById(long id) {
        return userCardQueueRepository.findOne(id);
    }
}
