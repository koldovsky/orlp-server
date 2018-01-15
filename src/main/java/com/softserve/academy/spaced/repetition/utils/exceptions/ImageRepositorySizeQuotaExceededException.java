package com.softserve.academy.spaced.repetition.utils.exceptions;

public class ImageRepositorySizeQuotaExceededException extends ApplicationException {

    private String defaultMessage = "You have exceeded your quota for uploading images.\n" +
            "You should delete some images before new upload.";

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
