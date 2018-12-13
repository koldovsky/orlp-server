package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.DeckOwnership;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.repository.DeckOwnershipRepository;
import com.softserve.academy.spaced.repetition.service.DeckOwnershipService;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckOwnershipServiceImpl implements DeckOwnershipService {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DeckOwnershipRepository deckOwnershipRepository;

    @Override
    public boolean checkIsBoughtDeck(Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        List<DeckOwnership> deckOwnershipList = deckOwnershipRepository.findAll();
        for (DeckOwnership deckOwnership : deckOwnershipList) {
            if (user.getId().equals(deckOwnership.getUserId()) && deckId.equals(deckOwnership.getDeckId())) {
                return true;
            }
        }
        return false;
    }
}
