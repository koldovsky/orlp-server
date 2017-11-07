package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserCardQueueRepository extends JpaRepository<UserCardQueue, Long> {
    UserCardQueue findUserCardQueueByUserIdAndCardId(Long userId, Long cardId);

    long countAllByUserIdEqualsAndDeckIdEqualsAndDateToRepeatBefore(Long userId, Long deckId, Date now);
}
