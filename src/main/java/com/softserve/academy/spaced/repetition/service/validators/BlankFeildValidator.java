package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.BlankFieldException;
import org.springframework.stereotype.Service;

@Service

public class BlankFeildValidator extends AbstractValidator <User> {
    public BlankFeildValidator() {
    }

    @Override
    public void validate(User user) throws BlankFieldException {
        if (user.getAccount().getPassword().isEmpty() || user.getAccount().getEmail().isEmpty()
                || user.getPerson().getFirstName().isEmpty()
                || user.getPerson().getLastName().isEmpty()) {
            throw new BlankFieldException();
        }
    }
}
