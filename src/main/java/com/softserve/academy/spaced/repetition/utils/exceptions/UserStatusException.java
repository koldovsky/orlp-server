package com.softserve.academy.spaced.repetition.utils.exceptions;

import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;

public class UserStatusException  extends  ApplicationException{
    private final AccountStatus accountStatus;

    public UserStatusException(AccountStatus accountStatus){
       this.accountStatus = accountStatus;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }
}
