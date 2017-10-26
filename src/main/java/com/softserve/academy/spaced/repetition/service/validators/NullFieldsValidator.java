package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.stereotype.Service;

@Service
public class NullFieldsValidator extends AbstractValidator <User> {
    public NullFieldsValidator() {
    }

    public void validate(User user) {
        if (user != null) {
            if (user.getPerson() == null && user.getAccount() != null) {
                throw new IllegalArgumentException("Blank fields is not required");

            }
        }
    }
}
