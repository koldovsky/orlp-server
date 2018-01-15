package com.softserve.academy.spaced.repetition.utils.exceptions;

public class NotOwnerOperationException extends ApplicationException {

    private String defaultMessage = "Operation is not allowed for current user!";

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
