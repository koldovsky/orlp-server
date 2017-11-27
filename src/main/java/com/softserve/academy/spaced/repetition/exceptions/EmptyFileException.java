package com.softserve.academy.spaced.repetition.exceptions;

public class EmptyFileException extends Exception {
    public EmptyFileException() {
    }

    public EmptyFileException(String message) {
        super(message);
    }
}
