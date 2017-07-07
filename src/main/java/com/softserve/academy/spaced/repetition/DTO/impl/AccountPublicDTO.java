package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.AccountPublic;
import com.softserve.academy.spaced.repetition.domain.Account;

/**
 * Created by Yevhen on 07.07.2017.
 */
public class AccountPublicDTO extends Account implements AccountPublic {
    public AccountPublicDTO() {
    }

    public AccountPublicDTO(String email) {
        super(email);
    }

    public AccountPublicDTO(Account account) {
        super(account.getEmail());
    }

}

