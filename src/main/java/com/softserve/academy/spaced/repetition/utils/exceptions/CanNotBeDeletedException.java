package com.softserve.academy.spaced.repetition.utils.exceptions;

public class CanNotBeDeletedException extends ApplicationException {

    private String message = "Current image is already in use!";

    public CanNotBeDeletedException() {}

    public CanNotBeDeletedException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
