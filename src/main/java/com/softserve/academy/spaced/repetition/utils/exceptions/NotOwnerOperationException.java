package com.softserve.academy.spaced.repetition.utils.exceptions;

public class NotOwnerOperationException extends ApplicationException {

    private String message = "Operation is not allowed for current user!";

    public NotOwnerOperationException() {}

    public NotOwnerOperationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
