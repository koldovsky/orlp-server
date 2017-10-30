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

    private static final EnumMap<AccountStatus, ResponseEntity<MessageDTO>> USER_STATUS_ERROR_RESPONSE = new EnumMap<>(AccountStatus.class);

    static{
        USER_STATUS_ERROR_RESPONSE.put(AccountStatus.DELETED, new ResponseEntity<MessageDTO>(new MessageDTO("Account with this email is deleted"), HttpStatus.LOCKED));
        USER_STATUS_ERROR_RESPONSE.put(AccountStatus.BLOCKED, new ResponseEntity<MessageDTO>(new MessageDTO("Account with this email is blocked"), HttpStatus.FORBIDDEN));
        USER_STATUS_ERROR_RESPONSE.put(AccountStatus.INACTIVE, new ResponseEntity<MessageDTO>(new MessageDTO("Account with this email is inactive"), HttpStatus.METHOD_NOT_ALLOWED));
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

    @ExceptionHandler(MailException.class)
    ResponseEntity <MessageDTO> handleMailException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Mail not sent"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(WrongFormatException.class)
    ResponseEntity <MessageDTO> handleWrongFormatException() {
        return new ResponseEntity <MessageDTO>(new MessageDTO("Not valid file format"), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserStatusException.class)
    ResponseEntity<MessageDTO> handleUserStatusException(UserStatusException userStatusException) {
        return USER_STATUS_ERROR_RESPONSE.get(userStatusException.getAccountStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity <MessageDTO> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return  ResponseEntity.badRequest().body(new MessageDTO(illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity <MessageDTO> handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        return new ResponseEntity <MessageDTO>(new MessageDTO(noSuchElementException.getMessage()), HttpStatus.NOT_FOUND);
    }

}
