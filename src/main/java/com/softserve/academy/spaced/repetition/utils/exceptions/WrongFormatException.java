package com.softserve.academy.spaced.repetition.utils.exceptions;

public class WrongFormatException extends ApplicationException {

    private String message = "Not valid file format!";

    public WrongFormatException() {}

    public WrongFormatException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
