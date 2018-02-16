package com.softserve.academy.spaced.repetition.controller.dto.impl.userProfileDTO;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Person;

public class PersonalInfoDTO extends DTO<Person> {

    public PersonalInfoDTO(Person person) {
        super(person);
    }

    public String getFirstName() {
        return getEntity().getFirstName();
    }

    public String getLastName() {
        return getEntity().getLastName();
    }
}
