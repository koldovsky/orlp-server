package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByAccountEmail(String email);
}
