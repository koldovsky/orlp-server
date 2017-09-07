package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardQueueRepository extends JpaRepository<UserCardQueue, Long> {

    UserCardQueue findUserCardQueueByAccountEmailAndCardId(String accountEmail, long cardId);
}
