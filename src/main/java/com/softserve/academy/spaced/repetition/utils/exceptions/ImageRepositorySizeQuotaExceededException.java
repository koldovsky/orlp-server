package com.softserve.academy.spaced.repetition.utils.exceptions;

public class ImageRepositorySizeQuotaExceededException extends ApplicationException {

    private String message = "You have exceeded your quota for uploading images.\n" +
            "You should delete some images before new upload.";

    public ImageRepositorySizeQuotaExceededException() {}

    public ImageRepositorySizeQuotaExceededException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
