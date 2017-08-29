package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.ObjectHasNullFieldsException;
import org.springframework.stereotype.Service;

@Service
public class NullFieldsValidator extends AbstractValidator <User> {
    public NullFieldsValidator() {
    }

    public void validate(User user) throws ObjectHasNullFieldsException {
        if (user != null) {
            if (user.getPerson() == null && user.getAccount() != null) {
                throw new ObjectHasNullFieldsException();
            }
        }
    }
}
