package com.softserve.academy.spaced.repetition.utils.validators;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import com.softserve.academy.spaced.repetition.utils.validators.annotations.PasswordNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordNotNullValidator implements ConstraintValidator<PasswordNotNull, Account> {

    @Override
    public void initialize(PasswordNotNull constraintAnnotation) {
        /**
         * Initializes the validator in preparation for isValid(Object, ConstraintValidatorContext) calls.
         * This method is guaranteed to be called before any use of this instance for validation.
         * @param constraintAnnotation
         */
    }

    @Override
    public boolean isValid(Account account, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = false;
        if (account.getAuthenticationType().equals(AuthenticationType.GOOGLE)
                || account.getAuthenticationType().equals(AuthenticationType.FACEBOOK)) {
            valid = true;
        } else if (account.getPassword() == null) {
            valid = false;
        }
        return valid;
    }
}
