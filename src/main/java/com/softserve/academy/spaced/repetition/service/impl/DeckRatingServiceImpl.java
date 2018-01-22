package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.utils.exceptions.UserStatusException;
import com.softserve.academy.spaced.repetition.service.DeckRatingService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRatingRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;

@Service
public class DeckRatingServiceImpl implements DeckRatingService{

    @Autowired
    private DeckRatingRepository deckRatingRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void addDeckRating(int rating, Long deckId) throws NotAuthorisedUserException, UserStatusException {
        User user = userService.getAuthorizedUser();
        userService.isUserStatusActive(user);
        Account account = user.getAccount();
        String email = account.getEmail();
        DeckRating deckRating = deckRatingRepository.findAllByAccountEmailAndDeckId(email, deckId);
        if (deckRating == null) {
            deckRating = new DeckRating();
        }
        Deck deck = deckRepository.findOne(deckId);
        deckRating.setAccountEmail(email);
        deckRating.setDeck(deck);
        deckRating.setRating(rating);
        deckRatingRepository.save(deckRating);
        double averageDeckRating = deckRatingRepository.findAverageDeckRatingByDeckId(deckId);
        deck.setRating(averageDeckRating);
    }

    @Override
    public DeckRating getDeckRatingById(Long id) {
        return deckRatingRepository.findOne(id);
    }

}
