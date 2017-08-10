package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.MessageDTO;
import com.softserve.academy.spaced.repetition.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<MessageDTO> handleLargeFileException() {
        return new ResponseEntity<>(new MessageDTO("File upload error: file is too large."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MoreThanOneTimeRateException.class)
    ResponseEntity<MessageDTO> handleMoreThanOneTimeRateException() {
        return new ResponseEntity<>(new MessageDTO("Object was rated more than one time."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NullPointerException.class)
    ResponseEntity<MessageDTO> handleIllegalValueOfRequestParameterException() {
        return new ResponseEntity<>(new MessageDTO("The value of request parameter is not valid!"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RatingsBadValueException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    String handleRatingsBadValueException(HttpServletRequest request, Throwable ex) {
        return "Rating can't be less than 0 and more than 5";
    }

    @ExceptionHandler(ImageRepositorySizeQuotaExceededException.class)
    ResponseEntity<MessageDTO> handleImageRepositorySizeQuotaExceededException() {
        return new ResponseEntity<>(new MessageDTO("You have exceeded your quota for uploading images. You should delete some images before new upload."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CanNotBeDeletedException.class)
    ResponseEntity<MessageDTO> handleCanNotBeDeletedException(){
        return new ResponseEntity<MessageDTO>(new MessageDTO("Current image is already in use!"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotOwnerOperationException.class)
    ResponseEntity<MessageDTO> handleNotOwnerOperationException(){
        return new ResponseEntity<MessageDTO>(new MessageDTO("Operation is not allowed for current user!"), HttpStatus.CONFLICT);
    }

}
