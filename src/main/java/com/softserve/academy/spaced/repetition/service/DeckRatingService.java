package com.softserve.academy.spaced.repetition.service;

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
    public void addDeckRating(DeckRating deckRating, Long deckId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        DeckRating deckRatingByAccountEmail = deckRatingRepository.findAllByAccountEmailAndDeck_Id(email, deckId);
        if (deckRatingByAccountEmail != null) {
            deckRating.setId(deckRatingByAccountEmail.getId());
        }
        Deck deck = deckRepository.findOne(deckId);
        deckRating.setAccountEmail(email);
        deckRating.setDeck(deck);
        deckRatingRepository.save(deckRating);
        double deckAverageRating = deckRatingRepository.findRatingByDeckId(deckId);
        deck.setRating(deckAverageRating);
        deckRepository.save(deck);
    }

    public DeckRating getDeckRatingById(Long deckId) {
        return deckRatingRepository.findOne(deckId);
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
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
