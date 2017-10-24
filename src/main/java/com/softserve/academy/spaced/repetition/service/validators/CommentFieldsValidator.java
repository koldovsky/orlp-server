package com.softserve.academy.spaced.repetition.service.validators;

import com.softserve.academy.spaced.repetition.exceptions.EmptyCommentTextException;
import org.springframework.stereotype.Service;

@Service
public class CommentFieldsValidator {

    public void validate(String commentText) throws EmptyCommentTextException {
        if (commentText.trim().isEmpty()) {
            throw new EmptyCommentTextException();
        }
    }
}
