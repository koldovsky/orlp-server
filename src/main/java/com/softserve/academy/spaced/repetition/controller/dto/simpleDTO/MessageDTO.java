package com.softserve.academy.spaced.repetition.controller.dto.simpleDTO;

public class MessageDTO {
    private String message;

    public MessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
