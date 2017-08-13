package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.UserAdminDTO;
import com.softserve.academy.spaced.repetition.domain.AccountStatus;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        Link link = linkTo(methodOn(ManageUserController.class).getAllUsers()).withSelfRel();
        List<UserAdminDTO> usersDTOList = DTOBuilder.buildDtoListForCollection(userList,
                UserAdminDTO.class, link);
        return new ResponseEntity<>(usersDTOList, HttpStatus.OK);
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<UserAdminDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Link link = linkTo(methodOn(ManageUserController.class).getUserById(id)).withSelfRel();
        UserAdminDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserAdminDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<UserAdminDTO> setUsersStatusBlocked(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusBlocked(id);
        Link link = linkTo(methodOn(ManageUserController.class).setUsersStatusBlocked(id)).withSelfRel();
        UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(userWithChangedStatus, UserAdminDTO.class, link);

        return new ResponseEntity<>(userAdminDTO, HttpStatus.OK);
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<UserAdminDTO> setUsersStatusDeleted(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusDeleted(id);
        Link link = linkTo(methodOn(ManageUserController.class).setUsersStatusDeleted(id)).withSelfRel();
        UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(userWithChangedStatus, UserAdminDTO.class, link);

        return new ResponseEntity<>(userAdminDTO, HttpStatus.OK);
    }

    @PostMapping("/api/admin/users/{id}")
    public ResponseEntity<UserAdminDTO> setUsersStatusActive(@PathVariable Long id) {
        User userWithChangedStatus = userService.setUsersStatusActive(id);
        Link link = linkTo(methodOn(ManageUserController.class).setUsersStatusActive(id)).withSelfRel();
        UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(userWithChangedStatus, UserAdminDTO.class, link);

        return new ResponseEntity<>(userAdminDTO, HttpStatus.OK);
    }

    @PostMapping("/api/admin/users/{userId}/deck/{deckId}")
    public ResponseEntity<UserAdminDTO> addExistingDeckToUsersFolder(@PathVariable("userId") Long userId, @PathVariable("deckId") Long deckId) {

        User user = userService.addExistingDeckToUsersFolder(userId, deckId);

        if (user != null) {
            Link link = linkTo(methodOn(ManageUserController.class).addExistingDeckToUsersFolder(userId, deckId)).withSelfRel();
            UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(user, UserAdminDTO.class, link);
            return new ResponseEntity<UserAdminDTO>(userAdminDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/admin/users/{userId}/deck/{deckId}")
    public ResponseEntity<UserAdminDTO> removeDeckFromUsersFolder(@PathVariable("userId") Long userId, @PathVariable("deckId") Long deckId) {

        User user = userService.removeDeckFromUsersFolder(userId, deckId);

        if (user != null) {
            Link link = linkTo(methodOn(ManageUserController.class).getUserById(userId)).withSelfRel();
            UserAdminDTO userAdminDTO = DTOBuilder.buildDtoForEntity(user, UserAdminDTO.class, link);
            return new ResponseEntity<UserAdminDTO>(userAdminDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
