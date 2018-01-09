package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.controller.utils.dto.DTOBuilder;
import com.softserve.academy.spaced.repetition.controller.utils.dto.impl.UserCardQueuePublicDTO;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserCardQueueController {
    @Autowired
    private UserCardQueueService userCardQueueService;

    @PutMapping("/api/private/decks/{deckId}/cards/{cardId}/queue")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#deckId, #cardId)")
    public ResponseEntity updateUserCardQueue(@PathVariable Long deckId,
                                              @PathVariable Long cardId,
                                              @RequestBody String status)
            throws NotAuthorisedUserException, IllegalArgumentException {
        userCardQueueService.updateUserCardQueue(deckId, cardId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/private/decks/{deckId}/cards-that-need-repeating/count")
    public ResponseEntity<Long> countCardsThatNeedRepeating(@PathVariable Long deckId) throws NotAuthorisedUserException {
        return ResponseEntity.ok(userCardQueueService.countCardsThatNeedRepeating(deckId));
    }

    @GetMapping("api/user/card/queue/{userCardQueueId}")
    public ResponseEntity<UserCardQueuePublicDTO> getUserCardQueueById(@PathVariable Long userCardQueueId) {
        UserCardQueue userCardQueue = userCardQueueService.getUserCardQueueById(userCardQueueId);
        Link selfLink = linkTo(methodOn(UserCardQueueController.class).getUserCardQueueById(userCardQueueId)).withSelfRel();
        UserCardQueuePublicDTO userCardQueuePublicDTO = DTOBuilder
                .buildDtoForEntity(userCardQueue, UserCardQueuePublicDTO.class, selfLink);
        return ResponseEntity.ok(userCardQueuePublicDTO);
    }
}
