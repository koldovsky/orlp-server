package com.softserve.academy.spaced.repetition.service.validators.PasswordMatchesAnnotation;


import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements Validator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

//    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

//    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

//    @Override
//    public void initialize(PasswordMatches constraintAnnotation) {
//    }
//
//    @Override
//    public boolean isValid(String password, ConstraintValidatorContext context) {
//        boolean valid = false;
//        try {
//            User user =  userService.getAuthorizedUser();
//            Account account = user.getAccount();
//            String pass = account.getPassword();
//            valid = passwordEncoder.matches(password, pass);
//        } catch (NotAuthorisedUserException ignored) {
//        }
//        return valid;
//    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
