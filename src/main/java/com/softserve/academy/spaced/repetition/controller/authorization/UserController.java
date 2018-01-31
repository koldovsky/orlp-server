package com.softserve.academy.spaced.repetition.controller.authorization;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CourseLinkDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.UserDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.UserLinksDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Person;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.utils.audit.Auditable;
import com.softserve.academy.spaced.repetition.utils.audit.AuditingAction;
import com.softserve.academy.spaced.repetition.utils.exceptions.ImageRepositorySizeQuotaExceededException;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.service.MailService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;

    @GetMapping("api/private/user/details")
    public ResponseEntity<UserDTO> getAuthorizedUserPublicInfo() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Link link = linkTo(methodOn(UserController.class).getAuthorizedUserWithLinks()).withSelfRel();
        UserDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("api/private/user/{user_id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long user_id) {
        User user = userService.getUserById(user_id);
        Link link = linkTo(methodOn(UserController.class).getUserById(user_id)).withSelfRel();
        UserDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("api/private/user")
    public ResponseEntity<UserLinksDTO> getAuthorizedUserWithLinks() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Link link = linkTo(methodOn(UserController.class).getAuthorizedUserWithLinks()).withSelfRel();
        UserLinksDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserLinksDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/private/user/{user_id}/courses")
    public ResponseEntity<List<CourseLinkDTO>> getAllCoursesByUserId(@PathVariable Long user_id) {
        Set<Course> set = userService.getAllCoursesByUserId(user_id);
        List<Course> courseList = new ArrayList<>(set);
        Link collectionLink = linkTo(methodOn(UserController.class).getAllCoursesByUserId(user_id)).withSelfRel();
        List<CourseLinkDTO> courses = DTOBuilder.buildDtoListForCollection(courseList, CourseLinkDTO.class, collectionLink);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("api/status")
    public ResponseEntity getUserStatus() throws UserStatusException {
        userService.getUserStatus();
        return new ResponseEntity(HttpStatus.OK);
    }
}
