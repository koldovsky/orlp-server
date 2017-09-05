package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.repository.CardRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private RatingCountService ratingCountService;

    @Transactional
    public void addCardRating(CardRating cardRating, Long deckId, Long cardId) {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        CardRating cardRatingByAccountEmail = cardRatingRepository.findCardRatingByAccountEmailAndCardIdAndDeckId(username, cardId, deckId);

        if (cardRatingByAccountEmail != null) {
            cardRating.setId(cardRatingByAccountEmail.getId());
        }
        cardRating.setAccountEmail(username);
        cardRating.setCardId(cardId);
        cardRating.setDeckId(deckId);
        cardRatingRepository.save(cardRating);

        if(cardRatingRepository.findRatingByCardId(cardId).size()>0) {
            Card card = cardRepository.findOne(cardId);
            Deck deck = deckRepository.findOne(deckId);
            double cardAvarageRating = ratingCountService.countAvarageRating(cardRatingRepository.findRatingByCardId(cardId));
            double deckAvarageRating = ratingCountService.countAvarageRating(cardRatingRepository.findRatingByDeckId(deckId));
            long numbOfUsersRatings = cardRatingRepository.countAllByCardId(cardId);
            card.setRating(cardAvarageRating);
            card.setNumbOfUsersRatings(numbOfUsersRatings);
            deck.setRating(deckAvarageRating);
            cardRepository.save(card);
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
