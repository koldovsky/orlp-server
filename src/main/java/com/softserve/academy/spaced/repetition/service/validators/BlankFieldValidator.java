package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.stereotype.Service;

@Service
public class BlankFieldValidator extends AbstractValidator <User> {
    public BlankFieldValidator() {
    }

    @Override
    public void validate(User user) {
        if (user.getAccount().getPassword().isEmpty() || user.getAccount().getEmail().isEmpty()
                || user.getPerson().getFirstName().isEmpty()
                || user.getPerson().getLastName().isEmpty()) {
            throw new IllegalArgumentException("Blank fields is not required");
        }
    }
}
