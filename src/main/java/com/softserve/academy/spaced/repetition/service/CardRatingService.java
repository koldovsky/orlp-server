package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CardRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardRatingService {

    @Autowired
    private CardRatingRepository cardRatingRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserService userService;

    public void addCardRating(CardRating cardRating, Long deckId, Long cardId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        CardRating cardRatingByAccountEmail = cardRatingRepository.findCardRatingByAccountEmailAndCardIdAndDeckId(email, cardId, deckId);

        if (cardRatingByAccountEmail != null) {
            cardRating.setId(cardRatingByAccountEmail.getId());
        }

        cardRating.setAccountEmail(email);
        cardRating.setCardId(cardId);
        cardRating.setDeckId(deckId);
        cardRatingRepository.save(cardRating);

        Card card = cardRepository.findOne(cardId);
        double cardAverageRating = cardRatingRepository.findRatingByCardId(cardId);
        card.setRating(cardAverageRating);
        Deck deck = deckRepository.findOne(deckId);
        double deckAverageRating = cardRatingRepository.findRatingByDeckId(deckId);
        deck.setRating(deckAverageRating);
        cardRepository.save(card);
    }

    public CardRating getCardRatingById(Long cardId) {
        return cardRatingRepository.findOne(cardId);
    }
}
