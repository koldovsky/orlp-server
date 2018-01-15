package com.softserve.academy.spaced.repetition.utils.exceptions;

public class WrongFormatException extends ApplicationException {

    private String defaultMessage = "Not valid file format!";

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
