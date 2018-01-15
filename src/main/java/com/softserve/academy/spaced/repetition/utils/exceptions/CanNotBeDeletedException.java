package com.softserve.academy.spaced.repetition.utils.exceptions;

public class CanNotBeDeletedException extends ApplicationException {

    private String defaultMessage = "Current image is already in use!";

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
