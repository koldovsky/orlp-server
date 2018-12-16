package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.controller.DeckCommentController;
import com.softserve.academy.spaced.repetition.domain.DeckOwnership;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.DeckOwnershipRepository;
import com.softserve.academy.spaced.repetition.service.DeckOwnershipService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckOwnershipServiceImpl implements DeckOwnershipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeckCommentController.class);

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DeckOwnershipRepository deckOwnershipRepository;

    @Override
    public boolean checkIsBoughtDeck(Long deckId) {
        try {
            User user = userService.getAuthorizedUser();
            List<DeckOwnership> deckOwnershipList = deckOwnershipRepository.findAll();
            for (DeckOwnership deckOwnership : deckOwnershipList) {
                if (user.getId().equals(deckOwnership.getUserId()) && deckId.equals(deckOwnership.getDeckId())) {
                    return true;
                }
            }
        } catch (NotAuthorisedUserException e) {
            LOGGER.warn("Operation is unavailable for unauthorized users!");
        }
        return false;
    }
}

