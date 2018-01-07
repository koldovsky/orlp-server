package com.softserve.academy.spaced.repetition.utils.validators;


import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.validators.annotations.PasswordMatches;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordMatchesValidator.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        /**
         * Initializes the validator in preparation for isValid(Object, ConstraintValidatorContext) calls.
         * This method is guaranteed to be called before any use of this instance for validation.
         * @param constraintAnnotation
         */
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean valid = false;
        try {
            valid = passwordEncoder.matches(password, userService.getAuthorizedUser().getAccount().getPassword());
        } catch (NotAuthorisedUserException ex) {
            LOGGER.trace("Ignored exception.", ex);
            LOGGER.warn("Operation is unavailable for unauthorized users!");
        }
        return valid;
    }
}
