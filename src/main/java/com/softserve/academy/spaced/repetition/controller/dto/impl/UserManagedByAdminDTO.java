package com.softserve.academy.spaced.repetition.controller.dto.impl;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTO;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.hateoas.Link;

public class UserManagedByAdminDTO extends DTO<User> {

    public UserManagedByAdminDTO(User user, Link link) {
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

    public AccountStatus getAccountStatus() {
        return getEntity().getAccount().getStatus();
    }

}
