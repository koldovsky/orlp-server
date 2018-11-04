package com.softserve.academy.spaced.repetition.controller;
import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.AccountDTO;
import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.service.AccountService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/profile")
public class UserProfileLearningController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileLearningController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/learning-details")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDTO> getLearningDetails() throws NotAuthorisedUserException {
        Account account = accountService.getAccountDetails();
        Link link = linkTo(methodOn(UserProfileLearningController.class).getLearningDetails()).withSelfRel();
        return ResponseEntity.ok(DTOBuilder.buildDtoForEntity(account, AccountDTO.class, link));
    }

    @PutMapping("/learning-details")
    @PreAuthorize("hasPermission('PROFILE_LEARNING','UPDATE')")
    public ResponseEntity<AccountDTO> updateLearningDetails(@RequestBody Account acc)
            throws NotAuthorisedUserException {
        LOGGER.debug("Updating of learning details");
        Account account = accountService.updateAccountDetails(acc);
        Link link = linkTo(methodOn(UserProfileLearningController.class).getLearningDetails()).withSelfRel();
        return ResponseEntity.ok(DTOBuilder.buildDtoForEntity(account, AccountDTO.class, link));
    }
}
