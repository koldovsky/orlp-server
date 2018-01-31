package com.softserve.academy.spaced.repetition.utils.validators;

import com.softserve.academy.spaced.repetition.repository.AccountRepository;
import com.softserve.academy.spaced.repetition.utils.validators.annotations.EmailNotUsed;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotExistValidator implements ConstraintValidator<EmailNotUsed, String> {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void initialize(EmailNotUsed constraintAnnotation) {
        /**
         * Initializes the validator in preparation for isValid(Object, ConstraintValidatorContext) calls.
         * This method is guaranteed to be called before any use of this instance for validation.
         * @param constraintAnnotation
         */
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return accountRepository.findByEmail(email) == null;
    }
}
