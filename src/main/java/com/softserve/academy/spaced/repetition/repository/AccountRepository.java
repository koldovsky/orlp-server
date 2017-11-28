package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByEmail(String email);

    public Account findById(long id);

    @Query("UPDATE Account acc set acc.status = :status WHERE id = :account_id")
    public void updateAccount(@Param("status") String status, @Param("account_id") long accountId);

    @Query("SELECT a.lastPasswordResetDate FROM Account a WHERE a.email = :email")
    Date getLastPasswordResetDate(@Param("email") String email);
}