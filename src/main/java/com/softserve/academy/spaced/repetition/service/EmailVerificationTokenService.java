package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.EmailVerificationToken;
import com.softserve.academy.spaced.repetition.repository.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationTokenService {
    @Autowired
    EmailVerificationTokenRepository emailVerificationTokenRepository;


    public EmailVerificationToken getVerificationToken(String verificationToken) {
        return emailVerificationTokenRepository.findByEmailToken(verificationToken);
    }

    public void createVerificationToken(Account account, String enteredToken) {
        EmailVerificationToken token = new EmailVerificationToken(account, enteredToken);
        emailVerificationTokenRepository.save(token);
    }
}
