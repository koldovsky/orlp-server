package com.softserve.academy.spaced.repetition.utils.exceptions;

public class PasswordCannotBeNullException extends ApplicationException {

    public PasswordCannotBeNullException() {
    }

    public PasswordCannotBeNullException(String message) {
        super(message);
    }
}
