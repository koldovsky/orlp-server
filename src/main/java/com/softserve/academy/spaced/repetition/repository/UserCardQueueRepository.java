package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserCardQueueRepository extends JpaRepository<UserCardQueue, Long> {
    UserCardQueue findUserCardQueueByAccountEmailAndCardId(String accountEmail, Long cardId);

    long countAllByAccountEmailEqualsAndDeckIdEqualsAndDateToRepeatBefore(String email, Long deckId, Date now);
}
