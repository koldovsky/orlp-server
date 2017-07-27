package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.exceptions.MoreThanOneTimeRateException;
import com.softserve.academy.spaced.repetition.repository.CardRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardRatingService {

    @Autowired
    private CardRatingRepository cardRatingRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    RatingCountService ratingCountService;

    public void addCardRating(CardRating cardRating, Long deckId, Long cardId) throws MoreThanOneTimeRateException {

        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String username = user.getUsername();

            CardRating cardRatingByAccountEmail = cardRatingRepository.findAllByAccountEmailAndCardId(username, cardId);

            if (cardRatingByAccountEmail == null) {

                Card card = cardRepository.findOne(cardId);
                Deck deck = deckRepository.findOne(deckId);
                cardRating.setAccountEmail(username);
                cardRating.setCardId(cardId);
                cardRating.setDeckId(deckId);
                cardRatingRepository.save(cardRating);

                double cardAvarageRating = ratingCountService.countAvarageRating(cardRatingRepository.findRatingByCardId(cardId));
                double deckAvarageRating = ratingCountService.countAvarageRating(cardRatingRepository.findRatingByDeckId(deckId));
                long numbOfUsersRatings = cardRatingRepository.countAllByCardId(cardId);
                card.setRating(cardAvarageRating);
                card.setNumbOfUsersRatings(numbOfUsersRatings);
                deck.setRating(deckAvarageRating);
                cardRepository.save(card);

            } else {
                throw new MoreThanOneTimeRateException();
            }
        }

    public List<CardRating> getAllCardRating() {
        List<CardRating> cardRatings = cardRatingRepository.findAll();
        return cardRatings;
    }

    public CardRating getCardRatingById(Long cardId) {
        CardRating cardRating = cardRatingRepository.findOne(cardId);
        return cardRating;
    }

}
