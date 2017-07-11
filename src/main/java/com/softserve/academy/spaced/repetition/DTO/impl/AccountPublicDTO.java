package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.domain.Account;

public class AccountPublicDTO extends DTO<Account> {
    public AccountPublicDTO(Account account) {
        super(account);
    }

    public String getEmail(){return getEntity().getEmail();}

}

