package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.CourseLinkDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.UserDTO;
import com.softserve.academy.spaced.repetition.controller.dto.impl.UserLinksDTO;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("api/user/details")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getAuthorizedUserPublicInfo() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Link link = linkTo(methodOn(UserController.class).getAuthorizedUserWithLinks()).withSelfRel();
        UserDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("api/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserLinksDTO> getAuthorizedUserWithLinks() throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        Link link = linkTo(methodOn(UserController.class).getAuthorizedUserWithLinks()).withSelfRel();
        UserLinksDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserLinksDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/private/user/{userId}/courses")
    @PreAuthorize("hasPermission('USER','READ') && principal.id==#userId")
    public ResponseEntity<List<CourseLinkDTO>> getAllCoursesByUserId(@PathVariable Long userId) {
        Set<Course> set = userService.getAllCoursesByUserId(userId);
        List<Course> courseList = new ArrayList<>(set);
        Link collectionLink = linkTo(methodOn(UserController.class).getAllCoursesByUserId(userId)).withSelfRel();
        List<CourseLinkDTO> courses = DTOBuilder.buildDtoListForCollection(courseList, CourseLinkDTO.class, collectionLink);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

}
