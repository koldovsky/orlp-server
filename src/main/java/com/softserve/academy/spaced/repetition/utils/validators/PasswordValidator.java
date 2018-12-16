package com.softserve.academy.spaced.repetition.utils.validators;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.enums.AuthenticationType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "authenticationType", "message.validation.fieldNotNull");
        Account account = (Account) o;
        if (!(account.getAuthenticationType().equals(AuthenticationType.GOOGLE)
                || account.getAuthenticationType().equals(AuthenticationType.FACEBOOK))
                && account.getPassword() == null) {
            errors.rejectValue("password", "message.validation.fieldNotNull");
        }
    }
}
