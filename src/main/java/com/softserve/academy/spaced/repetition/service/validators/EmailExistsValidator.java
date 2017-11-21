package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailExistsValidator {

    @Autowired
    private UserRepository userRepository;

    public boolean isValid(String email) {
        return userRepository.findUserByAccountEmail(email) == null;
    }
}
