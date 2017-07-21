package com.softserve.academy.spaced.repetition.service;

import ch.qos.logback.core.net.SyslogOutputStream;
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

import java.util.List;

@Service
public class CardRatingService {

    @Autowired
    private CardRatingRepository cardRatingRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private DeckRepository deckRepository;

    public void addCardRating(CardRating cardRating, Long deckId, Long cardId) {

        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();

        System.out.println("username " + username);
        CardRating cardRatingByAccountEmail = cardRatingRepository.findAllByAccountEmail(username);

        if (cardRatingByAccountEmail == null) {

            Card card = cardRepository.findOne(cardId);
            Deck deck = deckRepository.findOne(deckId);
            cardRating.setAccountEmail(username);
            cardRating.setCardId(cardId);
            cardRating.setDeckId(deckId);
            cardRatingRepository.save(cardRating);

            double cardAvarageRating = countCardAvarageRating(cardId);
            double deckAvarageRating = countDeckAvarageRating(deckId);
            card.setRating(cardAvarageRating);
            deck.setRating(deckAvarageRating);
            cardRepository.save(card);

        }

    }

    public double countCardAvarageRating(Long cardId) {
        double totalRating = 0;
        List<CardRating> cardRatings = cardRatingRepository.findAllByCardId(cardId);
        for (CardRating cRating : cardRatings) {
            totalRating += cRating.getRating();
        }
        totalRating = totalRating / (cardRatings.size());
        return totalRating;
    }

    public double countDeckAvarageRating(Long deckId) {
        double totalRating = 0;
        List<CardRating> deckRatings = cardRatingRepository.findAllByDeckId(deckId);
        for (CardRating dRating : deckRatings) {
            totalRating += dRating.getRating();
        }
        totalRating = totalRating / (deckRatings.size());
        return totalRating;
    }


    public List<CardRating> getAllCardRating() {
        List<CardRating> cardRatings = cardRatingRepository.findAll();
        return cardRatings;
    }

    public CardRating getCardRatingById(Long id) {
        CardRating cardRating = cardRatingRepository.findOne(id);
        return cardRating;
    }

    public void updateCardRating(Long id, long cardId) {
        double rating = countCardAvarageRating(cardId);
        Card card = cardRepository.findOne(id);
        card.setRating(rating);
        cardRepository.save(card);
    }
}
