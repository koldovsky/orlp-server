package com.softserve.academy.spaced.repetition.utils.exceptions;

public class EmptyFileException extends ApplicationException {
    public EmptyFileException() {
    }

    public EmptyFileException(String message) {
        super(message);
    }
}
