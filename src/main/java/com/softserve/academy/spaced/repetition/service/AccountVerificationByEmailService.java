package com.softserve.academy.spaced.repetition.service;


import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.exceptions.EmailDoesntExistException;
import com.softserve.academy.spaced.repetition.exceptions.ExpiredTokenForVerificationException;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import com.softserve.academy.spaced.repetition.security.JwtTokenForMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountVerificationByEmailService {
    @Autowired
    private JwtTokenForMail jwtTokenForMail;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public void accountVerification(String token) throws EmailDoesntExistException, ExpiredTokenForVerificationException {
        String email;
        email = jwtTokenForMail.decryptToken(token);
        if (userRepository.findUserByAccountEmail(email) == null) {
            throw new EmailDoesntExistException();
        }
        Account editedAcc = userRepository.findUserByAccountEmail(email).getAccount();
        if (editedAcc.getStatus().equals(AccountStatus.INACTIVE)) {
            editedAcc.setStatus(AccountStatus.ACTIVE);
        }
        accountRepository.save(editedAcc);
    }
}
