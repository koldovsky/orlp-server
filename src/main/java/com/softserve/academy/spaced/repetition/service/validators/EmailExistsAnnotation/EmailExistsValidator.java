package com.softserve.academy.spaced.repetition.service.validators.EmailExistsAnnotation;

import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailExistsValidator implements Validator{

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public void initialize(EmailExists constraintAnnotation) {
//    }
//
//    @Override
//    public boolean isValid(String email, ConstraintValidatorContext context) {
//        return userRepository.findUserByAccountEmail(email) == null;
//    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
