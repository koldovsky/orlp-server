package com.softserve.academy.spaced.repetition.service.validators.PasswordMatchesAnnotation;


import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, String> {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean valid = false;
        try {
            valid = passwordEncoder.matches(password, userService.getAuthorizedUser().getAccount().getPassword());
        } catch (NotAuthorisedUserException ignored) {
        } finally {
            return valid;
        }
    }
}
