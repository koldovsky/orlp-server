package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.FieldErrorDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.ValidationMessageDTO;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.MessageDTO;
import com.softserve.academy.spaced.repetition.domain.enums.AccountStatus;
import com.softserve.academy.spaced.repetition.utils.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();

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
        return new MessageDTO(messageSource.getMessage("message.exception.tooLargeFile", new Object[]{}, locale));
    }


    @ExceptionHandler(ImageRepositorySizeQuotaExceededException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    MessageDTO handleImageRepositorySizeQuotaExceededException() {
        return new MessageDTO(messageSource.getMessage("message.exception.exceededQuotaDeleteImage", new Object[]{}, locale));
    }

    @ExceptionHandler(CanNotBeDeletedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    MessageDTO handleCanNotBeDeletedException() {
        return new MessageDTO(messageSource.getMessage("message.exception.imageInUse", new Object[]{}, locale));
    }

    @ExceptionHandler(NotOwnerOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    MessageDTO handleNotOwnerOperationException() {
        return new MessageDTO(messageSource.getMessage("message.exception.forCurrentUserNotAllowed", new Object[]{}, locale));
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
        return new MessageDTO(messageSource.getMessage("message.exception.ipAdressCouldntBeDetermined", new Object[]{}, locale));
    }

    @ExceptionHandler(MailException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    MessageDTO handleMailException() {
        return new MessageDTO(messageSource.getMessage("message.exception.mailNotSent", new Object[]{}, locale));
    }

    @ExceptionHandler(WrongFormatException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    MessageDTO handleWrongFormatException() {
        return new MessageDTO(messageSource.getMessage("message.exception.notValidFileFormat", new Object[]{}, locale));
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
