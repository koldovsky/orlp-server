package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.EmailUniquesException;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailUniqiesValidator extends AbstractValidator <User> {
    @Autowired
    private UserRepository userRepository;

    public EmailUniqiesValidator() {
    }

    @Override
    protected void validate(User user) throws EmailUniquesException {
        if (userRepository.findUserByAccountEmail(user.getAccount().getEmail()) != null) {
            throw new EmailUniquesException();
        }
    }
}
