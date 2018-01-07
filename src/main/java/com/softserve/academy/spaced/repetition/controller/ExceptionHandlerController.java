package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.FieldErrorDTO;
import com.softserve.academy.spaced.repetition.controller.dto.ValidationMessageDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.MessageDTO;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.utils.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private static final EnumMap<AccountStatus,
            ResponseEntity<MessageDTO>> USER_STATUS_ERROR_RESPONSE = new EnumMap<>(AccountStatus.class);

    static {
        USER_STATUS_ERROR_RESPONSE.put(AccountStatus.DELETED,
                new ResponseEntity<>(new MessageDTO("Account with this email is deleted"),
                        HttpStatus.LOCKED));
        USER_STATUS_ERROR_RESPONSE.put(AccountStatus.BLOCKED,
                new ResponseEntity<>(new MessageDTO("Account with this email is blocked"),
                        HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    MessageDTO handleLargeFileException() {
        return new MessageDTO("File upload error: file is too large.");
    }


    @ExceptionHandler(ImageRepositorySizeQuotaExceededException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    MessageDTO handleImageRepositorySizeQuotaExceededException() {
        return new MessageDTO("You have exceeded your quota for uploading images. " +
                "You should delete some images before new upload.");
    }

    @ExceptionHandler(CanNotBeDeletedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    MessageDTO handleCanNotBeDeletedException() {
        return new MessageDTO("Current image is already in use!");
    }

    @ExceptionHandler(NotOwnerOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    MessageDTO handleNotOwnerOperationException() {
        return new MessageDTO("Operation is not allowed for current user!");
    }


    @ExceptionHandler(NotAuthorisedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    MessageDTO handleNotAuthorisedUserException() {
        return new MessageDTO("Operation is unavailable for unauthorized users!");
    }

    @ExceptionHandler(UnknownHostException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    MessageDTO handleUnknownHostException() {
        return new MessageDTO("The IP address of a host could not be determined");
    }

    @ExceptionHandler(MailException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    MessageDTO handleMailException() {
        return new MessageDTO("Mail not sent");
    }

    @ExceptionHandler(WrongFormatException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    MessageDTO handleWrongFormatException() {
        return new MessageDTO("Not valid file format");
    }

    @ExceptionHandler(UserStatusException.class)
    @ResponseBody
    MessageDTO handleUserStatusException(UserStatusException userStatusException) {
        return USER_STATUS_ERROR_RESPONSE.get(userStatusException.getAccountStatus()).getBody();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    MessageDTO handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return new MessageDTO(illegalArgumentException.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    MessageDTO handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        return new MessageDTO(noSuchElementException.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<FieldErrorDTO> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(p -> new FieldErrorDTO(p.getField(), p.getDefaultMessage())).collect(Collectors.toList());
        ValidationMessageDTO messageDTO = new ValidationMessageDTO(errors);
        return new ResponseEntity<>(messageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    MessageDTO handleLockedException(LockedException lockedException) {
        return new MessageDTO(lockedException.getMessage());
    }

    @ExceptionHandler(EmptyFileException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    MessageDTO handleEmptyFileException(EmptyFileException emptyFileException) {
        return new MessageDTO(emptyFileException.getMessage());
    }
}
