package com.softserve.academy.spaced.repetition.controller.utils.dto.impl;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import org.springframework.hateoas.Link;

public class AccountPublicDTO extends DTO<Account> {

    public AccountPublicDTO(Account account, Link link) {
        super(account, link);
    }

    public String getEmail() {
        return getEntity().getEmail();
    }
}

