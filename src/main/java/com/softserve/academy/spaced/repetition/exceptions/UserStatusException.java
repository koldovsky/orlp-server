package com.softserve.academy.spaced.repetition.exceptions;

import com.softserve.academy.spaced.repetition.domain.AccountStatus;

public class UserStatusException  extends  ApplicationException{
   private final AccountStatus accountStatus;

   public UserStatusException(AccountStatus accountStatus){
       this.accountStatus = accountStatus;
   }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }
}
