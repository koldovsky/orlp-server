package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class EmailUniquesValidator extends AbstractValidator <User> {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void validate(User user) {
        if (userRepository.findUserByAccountEmail(user.getAccount().getEmail()) != null) {
            throw new NoSuchElementException("Email exists");
        }
    }
}
