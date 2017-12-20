package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.exceptions.UserStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRatingRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;

@Service
public class DeckRatingService {

    private DeckRatingRepository deckRatingRepository;

    private DeckRepository deckRepository;

    private UserService userService;

    @Transactional
    public void addDeckRating(int rating, Long deckId) throws NotAuthorisedUserException, UserStatusException {
        User user = userService.getAuthorizedUser();
        userService.isUserStatusActive(user);
        String email = user.getAccount().getEmail();
        DeckRating deckRating = deckRatingRepository.findAllByAccountEmailAndDeck_Id(email, deckId);
        if (deckRating == null) {
            deckRating = new DeckRating();
        }
        Deck deck = deckRepository.findOne(deckId);
        deckRating.setAccountEmail(email);
        deckRating.setDeck(deck);
        deckRating.setRating(rating);
        deckRatingRepository.save(deckRating);
        double deckAverageRating = deckRatingRepository.findRatingByDeckId(deckId);
        deck.setRating(deckAverageRating);
        deckRepository.save(deck);
    }

    public DeckRating getDeckRatingById(Long id) {
        return deckRatingRepository.findOne(id);
    }

    @Autowired
    public void setDeckRatingRepository(DeckRatingRepository deckRatingRepository) {
        this.deckRatingRepository = deckRatingRepository;
    }

    @Autowired
    public void setDeckRepository(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    @Autowired
    public void setUserServiceImpl(UserService userService) {
        this.userService = userService;
    }
}
