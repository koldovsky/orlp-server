package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.UserCardQueuePublicDTO;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
public class UserCardQueueController {

    @Autowired
    private UserCardQueueService userCardQueueService;

    @PostMapping("/api/user/deck/{deck_id}/card/{card_id}/queue")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#deck_id, #card_id)")
    public ResponseEntity<UserCardQueuePublicDTO> addUserCardQueue(@RequestBody UserCardQueue userCardQueue, @PathVariable long card_id, @PathVariable long deck_id) throws NotAuthorisedUserException {
        userCardQueueService.addUserCardQueue(userCardQueue, card_id, deck_id);
        Link selfLink = linkTo(methodOn(UserCardQueueController.class).getUserCardQueueById(userCardQueue.getId())).withSelfRel();
        UserCardQueuePublicDTO userCardQueuePublicDTO = DTOBuilder.buildDtoForEntity(userCardQueue, UserCardQueuePublicDTO.class, selfLink);
        return new ResponseEntity<>(userCardQueuePublicDTO, HttpStatus.CREATED);
    }

    @GetMapping("api/user/card/queue/{user_card_queue_id}")
    public ResponseEntity<UserCardQueuePublicDTO> getUserCardQueueById(@PathVariable long user_card_queue_id) {
        UserCardQueue userCardQueue = userCardQueueService.getUserCardQueueById(user_card_queue_id);
        Link selfLink = linkTo(methodOn(UserCardQueueController.class).getUserCardQueueById(user_card_queue_id)).withSelfRel();
        UserCardQueuePublicDTO userCardQueuePublicDTO = DTOBuilder.buildDtoForEntity(userCardQueue, UserCardQueuePublicDTO.class, selfLink);
        return new ResponseEntity<>(userCardQueuePublicDTO, HttpStatus.OK);
    }
}
