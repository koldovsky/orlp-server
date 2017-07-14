package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.exceptions.ExpiredTokenForVerificationException;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtTokenForMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountVerificationByEmailService {
    @Autowired
    JwtTokenForMail jwtTokenForMail;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity accountVerification(String token) {
        String email;
        try {
            email = jwtTokenForMail.decryptToken(token);
        } catch (ExpiredTokenForVerificationException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (userRepository.findUserByAccount_Email(email) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Account editedAcc = userRepository.findUserByAccount_Email(email).getAccount();
        if (editedAcc.getStatus().equals(AccountStatus.INACTIVE)) {
            editedAcc.setStatus(AccountStatus.ACTIVE);
        }
        accountRepository.save(editedAcc);
        return new ResponseEntity(HttpStatus.OK);
    }
}
