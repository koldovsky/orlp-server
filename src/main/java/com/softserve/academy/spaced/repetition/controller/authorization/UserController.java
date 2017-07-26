package com.softserve.academy.spaced.repetition.controller.authorization;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.UserLinksDTO;
import com.softserve.academy.spaced.repetition.DTO.impl.UserPublicDTO;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("api/private/user/details")
    public ResponseEntity<UserPublicDTO> getAuthorizedUserPublicInfo() {
        User user = userService.getAuthorizedUser();
        Link link = linkTo(methodOn(UserController.class).getAuthorizedUserWithLinks()).withSelfRel();
        UserPublicDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserPublicDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("api/private/user")
    public ResponseEntity<UserLinksDTO> getAuthorizedUserWithLinks() {
        User user = userService.getAuthorizedUser();
        Link link = linkTo(methodOn(UserController.class).getAuthorizedUserWithLinks()).withSelfRel();
        UserLinksDTO userDTO = DTOBuilder.buildDtoForEntity(user, UserLinksDTO.class, link);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
