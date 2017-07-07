package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Yevhen on 06.07.2017.
 */
@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;


    public User getUser(Long user_id) {
        return userRepository.findOne(user_id);
    }


    public boolean validateEmail(String email) {
        if (email != null && email.equals("")) {
            if (accountRepository.findByEmail(email) != null) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user, boolean isValid) {
        userRepository.save(user);
    }


}
