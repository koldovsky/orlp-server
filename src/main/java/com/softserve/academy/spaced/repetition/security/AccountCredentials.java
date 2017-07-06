package com.softserve.academy.spaced.repetition.security;

/**
 * Created by jarki on 7/4/2017.
 */
public class AccountCredentials {
    private String username;
    private String password;

    public AccountCredentials() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
