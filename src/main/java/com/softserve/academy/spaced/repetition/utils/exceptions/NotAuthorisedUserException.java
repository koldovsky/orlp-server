package com.softserve.academy.spaced.repetition.utils.exceptions;

public class NotAuthorisedUserException extends ApplicationException {

    private String message = "Operation is unavailable for unauthorized users!";

    public NotAuthorisedUserException() {}

    public NotAuthorisedUserException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
