package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.PasswordDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.UserDTO;
import com.softserve.academy.spaced.repetition.audit.Auditable;
import com.softserve.academy.spaced.repetition.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.controller.authorization.UserController;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.*;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserProfileController {

    @Autowired
    UserService userService;

    @Auditable(action = AuditingAction.EDIT_PERSONAL_DATA)
    @PutMapping(value = "/api/private/user/data")
    public ResponseEntity editPersonalData(@RequestBody Person person)throws NotAuthorisedUserException {
        userService.editPersonalData(person);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auditable(action =  AuditingAction.CHANGE_PASSWORD)
    @PutMapping(value = "/api/private/user/password-change")
    public ResponseEntity changePassword (@RequestBody PasswordDTO passwordDTO) throws NotAuthorisedUserException {
        userService.changePassword(passwordDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Auditable(action = AuditingAction.UPLOAD_IMAGE_PROFILE)
    @PostMapping("/api/private/user/image")
    public ResponseEntity uploadImageProfile(@RequestParam("file")MultipartFile file) throws ImageRepositorySizeQuotaExceededException,
            NotAuthorisedUserException{
        User user=userService.uploadImage(file);
        return new ResponseEntity( HttpStatus.OK);
    }

    @GetMapping(value = "/api/private/user/image", produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getProfileImage() throws NotAuthorisedUserException {
        byte[] imageContentBytes = userService.getDecodedImageContent();
        if (imageContentBytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageContentBytes);
    }

    @Auditable(action = AuditingAction.DELETE_PROFILE)
    @GetMapping("/api/private/user/delete")
    public ResponseEntity deleteProfile() throws NotAuthorisedUserException{
        userService.deleteAccount();
        return new ResponseEntity(HttpStatus.OK);
    }
}
