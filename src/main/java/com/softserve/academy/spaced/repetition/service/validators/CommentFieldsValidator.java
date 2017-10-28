package com.softserve.academy.spaced.repetition.service.validators;

import org.springframework.stereotype.Service;

@Service
public class CommentFieldsValidator {

    public void validate(String commentText) {
        if (commentText.trim().isEmpty()) {
            throw new IllegalArgumentException("Current comment can not be empty");
        }
    }
}
