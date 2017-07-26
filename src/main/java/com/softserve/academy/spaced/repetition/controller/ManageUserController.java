package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.impl.UserAdminDTO;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class ManageUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/admin/users")
    public ResponseEntity<List<UserAdminDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        Link collectionLink = linkTo(methodOn(ManageUserController.class).getAllUsers()).withSelfRel();
        List<UserAdminDTO> usersDTOList = DTOBuilder.buildDtoListForCollection(userList,
                UserAdminDTO.class, collectionLink);
        return new ResponseEntity<>(usersDTOList, HttpStatus.OK);
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<UserAdminDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Link collectionLink = linkTo(methodOn(ManageUserController.class).getUserById(id)).withSelfRel();
        UserAdminDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserAdminDTO.class, collectionLink);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<UserAdminDTO> toggleUsersBlockStatus(@PathVariable Long id) {
        User userWithChangedStatus = userService.toggleUsersStatus(id, AccountStatus.BLOCKED);
        Link collectionLink = linkTo(methodOn(ManageUserController.class).toggleUsersBlockStatus(id)).withSelfRel();
        UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(userWithChangedStatus, UserAdminDTO.class, collectionLink);

        return new ResponseEntity<>(userAdminDTO, HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<UserAdminDTO> toggleUsersDeleteStatus(@PathVariable Long id) {
        User userWithChangedStatus = userService.toggleUsersStatus(id, AccountStatus.DELETED);
        Link collectionLink = linkTo(methodOn(ManageUserController.class).toggleUsersDeleteStatus(id)).withSelfRel();
        UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(userWithChangedStatus, UserAdminDTO.class, collectionLink);

        return new ResponseEntity<>(userAdminDTO, HttpStatus.OK);
    }

    @PostMapping("/api/admin/users/{userId}/deck/{deckId}")
    public ResponseEntity<UserAdminDTO> addExistingDeckToUsersFolder(@PathVariable("userId") Long userId, @PathVariable("deckId") Long deckId) {

        User user = userService.addExistingDeckToUsersFolder(userId, deckId);

        if (user != null) {
            Link collectionLink = linkTo(methodOn(ManageUserController.class).addExistingDeckToUsersFolder(userId, deckId)).withSelfRel();
            UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(user, UserAdminDTO.class, collectionLink);
            return new ResponseEntity<UserAdminDTO>(userAdminDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
