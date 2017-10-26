package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.Person;
import org.springframework.stereotype.Service;

@Service
public class DataFieldValidator  {
    public DataFieldValidator() {
    }

    public void validate(Person person)  {
        if (person.getFirstName().isEmpty()
                || person.getLastName().isEmpty()) {
            throw new IllegalArgumentException("First name or last name can not be empty");
        }
    }
}