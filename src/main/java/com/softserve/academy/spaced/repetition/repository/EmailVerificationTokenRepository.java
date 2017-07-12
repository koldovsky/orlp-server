package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmailVerificationTokenRepository extends JpaRepository <EmailVerificationToken, Long> {

    public EmailVerificationToken findByAccount(Account account);

    public EmailVerificationToken findByEmailToken(String token);
}
