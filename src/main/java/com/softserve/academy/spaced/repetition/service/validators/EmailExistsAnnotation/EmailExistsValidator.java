package com.softserve.academy.spaced.repetition.service.validators.EmailExistsAnnotation;

import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(EmailExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.findUserByAccountEmail(email) == null;
    }
}
