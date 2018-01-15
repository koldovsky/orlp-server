package com.softserve.academy.spaced.repetition.utils.exceptions;

public class NotAuthorisedUserException extends ApplicationException {

    private String defaultMessage = "Operation is unavailable for unauthorized users!";

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
