package com.softserve.academy.spaced.repetition.DTO.impl;

import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.controller.CourseController;
import com.softserve.academy.spaced.repetition.controller.FolderController;
import com.softserve.academy.spaced.repetition.controller.authorization.UserController;
import com.softserve.academy.spaced.repetition.domain.User;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserLinksDTO extends DTO<User> {

    public UserLinksDTO(User entity, Link link) {
        super(entity, link);

        Link linkFolder = linkTo(methodOn(FolderController.class).getAllDecksWithFolder(getEntity().getFolder().getId())).withRel("folder");
        add(getLinkWithReplacedParentPart(linkFolder).withRel("folder"));
        add(linkTo(methodOn(UserController.class).getAllCoursesByUserId(getEntity().getId())).withRel("courses"));

    }

    public Long getUserId() {
        return getEntity().getId();
    }

    public String getFirstName() { return getEntity().getPerson().getFirstName(); }

    public String getLastName() { return getEntity().getPerson().getLastName(); }

    public String getEmail() { return getEntity().getAccount().getEmail(); }

}
