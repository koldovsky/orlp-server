package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.MessageDTO;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.*;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private static final EnumMap<AccountStatus, ResponseEntity<MessageDTO>> MAP = new EnumMap<AccountStatus, ResponseEntity<MessageDTO>>(AccountStatus.class);

    static{
        MAP.put(AccountStatus.DELETED, new ResponseEntity<MessageDTO>(new MessageDTO("Account with this email is deleted"), HttpStatus.LOCKED));
        MAP.put(AccountStatus.BLOCKED, new ResponseEntity<MessageDTO>(new MessageDTO("Account with this email is blocked"), HttpStatus.FORBIDDEN));
        MAP.put(AccountStatus.INACTIVE, new ResponseEntity<MessageDTO>(new MessageDTO("Account with this email is inactive"), HttpStatus.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(MultipartException.class)
    ResponseEntity <MessageDTO> handleLargeFileException() {
        return new ResponseEntity <>(new MessageDTO("File upload error: file is too large."), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class)
    ResponseEntity <MessageDTO> handleIllegalValueOfRequestParameterException() {
        return new ResponseEntity <>(new MessageDTO("The value of request parameter is not valid!"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RatingsBadValueException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    String handleRatingsBadValueException(HttpServletRequest request, Throwable ex) {
        return "Rating can't be less than 1 and more than 5";
    }

    @ExceptionHandler(ImageRepositorySizeQuotaExceededException.class)
    ResponseEntity <MessageDTO> handleImageRepositorySizeQuotaExceededException() {
        return new ResponseEntity <>(new MessageDTO("You have exceeded your quota for uploading images. You should delete some images before new upload."), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CanNotBeDeletedException.class)
    ResponseEntity <MessageDTO> handleCanNotBeDeletedException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Current image is already in use!"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotOwnerOperationException.class)
    ResponseEntity <MessageDTO> handleNotOwnerOperationException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Operation is not allowed for current user!"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotAuthorisedUserException.class)
    ResponseEntity <MessageDTO> handleNotAuthorisedUserException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Operation is unavailable for unauthorized users!"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnknownHostException.class)
    ResponseEntity <MessageDTO> handleUnknownHostException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("The IP address of a host could not be determined"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BlankFieldException.class)
    ResponseEntity <MessageDTO> handleBlanckFieldException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Blank fields is not required"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailUniquesException.class)
    ResponseEntity <MessageDTO> handleEmailUniquesException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Email exists"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MailException.class)
    ResponseEntity <MessageDTO> handleMailException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Mail not sent"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ExpiredTokenForVerificationException.class)
    ResponseEntity <MessageDTO> handlExpiredTokenForVerificationException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("No token found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailDoesntExistException.class)
    ResponseEntity <MessageDTO> handleEmailDoesntExistException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Email not exists"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectHasNullFieldsException.class)
    ResponseEntity <MessageDTO> handleObjectHasNullFieldsException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Blank fields is not required"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileConnectionException.class)
    ResponseEntity <MessageDTO> handleFileConnectionException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Can't read data from uploaded file"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongFormatException.class)
    ResponseEntity <MessageDTO> handleWrongFormatException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Not valid file format"), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchFileException.class)
    ResponseEntity <MessageDTO> handleNoSuchFileException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Such file not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SameDeckInCourseException.class)
    ResponseEntity <MessageDTO> sameDeckInCourse() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Such deck already exists"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchDeckException.class)
    ResponseEntity <MessageDTO> handleNoSuchDeckException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Such deck not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileIsNotAnImageException.class)
    ResponseEntity <MessageDTO> handleFileIsNotAnImageException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("File upload error: file is not an image"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataFieldException.class)
    ResponseEntity <MessageDTO> handleDataFieldException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("First name or last name can not be empty"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordFieldException.class)
    ResponseEntity <MessageDTO> handlePasswordFieldException() {
        return new ResponseEntity<MessageDTO>(new MessageDTO("Current password must match and fields of password can not be empty"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserStatusException.class)
    ResponseEntity<MessageDTO> handleUserStatusException(UserStatusException userStatusException) {
        return MAP.get(userStatusException.getAccountStatus());
    }
    @ExceptionHandler(EmptyCommentTextException.class)
    ResponseEntity <MessageDTO> handleEmptyCommentTextException() {
        return new ResponseEntity<MessageDTO>(new MessageDTO("Current comment can not be empty"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CardContainsEmptyFieldsException.class)
    ResponseEntity <MessageDTO> handleCardContainsEmptyFieldsException() {
        return new ResponseEntity<MessageDTO>(new MessageDTO("All of card fields must be filled"), HttpStatus.FORBIDDEN);
    }



}
