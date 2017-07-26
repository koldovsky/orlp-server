package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.MessageDTO;
import com.softserve.academy.spaced.repetition.exceptions.ImageContextDublicationException;
import com.softserve.academy.spaced.repetition.exceptions.ImageNameDublicationException;
import com.softserve.academy.spaced.repetition.exceptions.MoreThanOneTimeRateException;
import com.softserve.academy.spaced.repetition.exceptions.RatingsBadValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    ResponseEntity<MessageDTO> handleLargeFileException() {
        return new ResponseEntity<>(new MessageDTO("File upload error: file is too large."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ImageNameDublicationException.class)
    ResponseEntity<MessageDTO> handleImageNameDublicateException() {
        return new ResponseEntity<>(new MessageDTO("File upload error: the name of file is already in use."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ImageContextDublicationException.class)
    ResponseEntity<MessageDTO> handleImageContextDublicateException() {
        return new ResponseEntity<>(new MessageDTO("File upload error: the file is already in use."), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(MoreThanOneTimeRateException.class)
    ResponseEntity<MessageDTO> handleMoreThanOneTimeRateException() {
        return new ResponseEntity<>(new MessageDTO("Object was rated more than one time."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<MessageDTO> handleIllegalValueOfRequestParameterException(){
        return new ResponseEntity<>(new MessageDTO("The value of request parameter is not valid!"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RatingsBadValueException.class)
    @ResponseBody
    @ResponseStatus(value=HttpStatus.CONFLICT)
    String handleRatingsBadValueException(HttpServletRequest request, Throwable ex) {
        return "Rating can't be less than 0 and more than 5";
    }
}
