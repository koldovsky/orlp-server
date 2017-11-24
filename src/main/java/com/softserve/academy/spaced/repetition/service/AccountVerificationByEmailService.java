package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtTokenForMail;
import com.softserve.academy.spaced.repetition.security.JwtTokenForResetPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AccountVerificationByEmailService {
    @Autowired
    private JwtTokenForMail jwtTokenForMail;
    @Autowired
    private JwtTokenForResetPassword jwtTokenForResetPassword;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public void accountVerification(String token) {
        String email;
        email = jwtTokenForMail.decryptToken(token);
        if (userRepository.findUserByAccountEmail(email) == null) {
            throw new NoSuchElementException("Email not exists");
        }
        Account editedAcc = userRepository.findUserByAccountEmail(email).getAccount();
        if (editedAcc.getStatus().equals(AccountStatus.INACTIVE)) {
            editedAcc.setStatus(AccountStatus.ACTIVE);
        }
        accountRepository.save(editedAcc);
    }

    public void verificationTokenForResetPassword(String token) {
        String identifier = jwtTokenForResetPassword.decryptToken(token);
        if (accountRepository.findAccountByIdentifier(identifier) == null) {
            throw new NoSuchElementException("Token is invalid");
        }
    }
}
