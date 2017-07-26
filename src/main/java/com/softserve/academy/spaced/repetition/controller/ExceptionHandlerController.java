package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.exceptions.ImageContextDublicationException;
import com.softserve.academy.spaced.repetition.exceptions.ImageNameDublicationException;
import com.softserve.academy.spaced.repetition.exceptions.MoreThanOneTimeRateException;
import com.softserve.academy.spaced.repetition.exceptions.RatingsBadValueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    @ResponseStatus(value= HttpStatus.CONFLICT)
    String handleLargeFileException(HttpServletRequest request, Throwable ex) {
        return "File upload error: file is too large.";
    }

    @ExceptionHandler(ImageNameDublicationException.class)
    @ResponseBody
    @ResponseStatus(value=HttpStatus.CONFLICT)
    String handleImageNameDublicateException(HttpServletRequest request, Throwable ex) {
        return "File upload error: the name of file is already in use.";
    }

    @ExceptionHandler(ImageContextDublicationException.class)
    @ResponseBody
    @ResponseStatus(value=HttpStatus.CONFLICT)
    String handleImageContextDublicateException(HttpServletRequest request, Throwable ex) {
        return "File upload error: the file is already in use.";
    }


    //
    @ExceptionHandler(MoreThanOneTimeRateException.class)
    @ResponseBody
    @ResponseStatus(value=HttpStatus.CONFLICT)
    String handleMoreThanOneTimeRateException(HttpServletRequest request, Throwable ex) {
        return "Object was rated more than one time.";
    }

    @ExceptionHandler(RatingsBadValueException.class)
    @ResponseBody
    @ResponseStatus(value=HttpStatus.CONFLICT)
    String handleRatingsBadValueException(HttpServletRequest request, Throwable ex) {
        return "Rating can't be less than 0 and more than 5";
    }
}
