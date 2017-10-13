package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.exceptions.DataFieldException;
import org.springframework.stereotype.Service;

@Service
public class DataFieldValidator  {
    public DataFieldValidator() {
    }

    public void validate(Person person) throws DataFieldException {
        if (person.getFirstName().isEmpty()
                || person.getLastName().isEmpty()) {
            throw new DataFieldException();
        }
    }
}