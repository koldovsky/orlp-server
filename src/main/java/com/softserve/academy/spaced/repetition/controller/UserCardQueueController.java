package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.dto.builder.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.dto.impl.UserCardQueuePublicDTO;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
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
@RequestMapping("api")
public class UserCardQueueController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserCardQueueService userCardQueueService;

    @PutMapping("/decks/{deckId}/cards/{cardId}/queue")
    @PreAuthorize("hasPermission('CARD_QUEUE', 'UPDATE')")
    public ResponseEntity updateUserCardQueue(@PathVariable Long deckId,
                                              @PathVariable Long cardId,
                                              @RequestBody String status)
            throws NotAuthorisedUserException, IllegalArgumentException {
        LOGGER.debug("Updating queue of user card with id: {} from deck with id: {}", cardId, deckId);
        userCardQueueService.updateUserCardQueue(deckId, cardId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/decks/{deckId}/cards-that-need-repeating/count")
    @PreAuthorize("hasPermission('CARD_QUEUE','READ')")
    public ResponseEntity<Long> countCardsThatNeedRepeating(@PathVariable Long deckId) throws NotAuthorisedUserException {
        return ResponseEntity.ok(userCardQueueService.countCardsThatNeedRepeating(deckId));
    }

    @GetMapping("/user/card/queue/{userCardQueueId}")
    @PreAuthorize("hasPermission('CARD_QUEUE','READ')")
    public ResponseEntity<UserCardQueuePublicDTO> getUserCardQueueById(@PathVariable Long userCardQueueId) {
        UserCardQueue userCardQueue = userCardQueueService.getUserCardQueueById(userCardQueueId);
        Link selfLink = linkTo(methodOn(UserCardQueueController.class).getUserCardQueueById(userCardQueueId)).withSelfRel();
        UserCardQueuePublicDTO userCardQueuePublicDTO = DTOBuilder
                .buildDtoForEntity(userCardQueue, UserCardQueuePublicDTO.class, selfLink);
        return ResponseEntity.ok(userCardQueuePublicDTO);
    }
}
