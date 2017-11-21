package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordMatchesValidator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public boolean isValid(String password) {
        boolean valid = false;
        try {
            valid = passwordEncoder.matches(password, userService.getAuthorizedUser().getAccount().getPassword());
        } catch (NotAuthorisedUserException ignored) {
        }
        return valid;
    }

}
