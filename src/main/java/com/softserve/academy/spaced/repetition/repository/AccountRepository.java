package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
     Account findByEmail(String email);

    @Query("SELECT a.lastPasswordResetDate FROM Account a WHERE a.email = :email")
    Date getLastPasswordResetDate(@Param("email") String email);
}