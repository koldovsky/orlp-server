package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.UserPublicDTO;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class ManageUserController {

    @Autowired
    private UserService userService;


    @GetMapping("/api/admin/users")
    public ResponseEntity<List<UserPublicDTO>> getUsersByFirstName() {

        List<User> userList = userService.getAllUsers();
        Link collectionLink = linkTo(methodOn(ManageUserController.class).getUsersByFirstName()).withSelfRel();
        List<UserPublicDTO> usersFilteredByFirstName = DTOBuilder.buildDtoListForCollection(userList,
                UserPublicDTO.class, collectionLink);

        return new ResponseEntity<>(usersFilteredByFirstName, HttpStatus.OK);

    }

    public Boolean toggleUsersActiveState() {
        return null;
    }

    public Boolean toggleUsersBanState() {
        return null;
    }

}
