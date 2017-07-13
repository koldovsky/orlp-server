package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.hateoas.Link;

public class UserPublicDTO extends DTO<User> {

    public UserPublicDTO(User user, Link link) {
        super(user, link);
    }

    public String getFirstName() {
        return getEntity().getPerson().getFirstName();
    }

    public String getLastName() {
        return getEntity().getPerson().getLastName();
    }

    public String getEmail() {
        return getEntity().getAccount().getEmail();
    }

}
