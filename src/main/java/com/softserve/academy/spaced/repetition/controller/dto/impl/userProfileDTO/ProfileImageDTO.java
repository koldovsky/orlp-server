package com.softserve.academy.spaced.repetition.controller.dto.impl.userProfileDTO;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.Person;

public class ProfileImageDTO extends DTO<Person> {

    public ProfileImageDTO(Person person) {
        super(person);
    }

    public String getImageBase64() {
        return getEntity().getImageBase64();
    }
}
