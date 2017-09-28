package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.DeckRatingRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeckRatingService {

    @Autowired
    private DeckRatingRepository deckRatingRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
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
}
