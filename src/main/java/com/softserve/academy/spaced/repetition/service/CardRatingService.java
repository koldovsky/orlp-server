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

    public void addCardRating(CardRating cardRating, Long deckId, Long cardId) throws MoreThanOneTimeRateException {

        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user==null)
            System.out.println();

        String username = user.getUsername();


        CardRating cardRatingByAccountEmail = cardRatingRepository.findAllByAccountEmailAndCardId(username, cardId);

       if (cardRatingByAccountEmail == null) {

            Card card = cardRepository.findOne(cardId);
            Deck deck = deckRepository.findOne(deckId);
            cardRating.setAccountEmail(username);
            cardRating.setCardId(cardId);
            cardRating.setDeckId(deckId);
            cardRatingRepository.save(cardRating);

            double cardAvarageRating = countAvarageRating(cardRatingRepository.findRatingByCardId(cardId));
            double deckAvarageRating = countAvarageRating(cardRatingRepository.findRatingByDeckId(deckId));
        System.out.println("cardAvgRate="+cardAvarageRating);
           System.out.println("deckAvgRate="+deckAvarageRating);
          long numbOfUsersRatings = countNumbOfUsersRatings(cardId);
            card.setRating(cardAvarageRating);
            card.setNumbOfUsersRatings(numbOfUsersRatings);
            deck.setRating(deckAvarageRating);
            cardRepository.save(card);

        } else {
            throw new MoreThanOneTimeRateException();
        }
    }

    public double countAvarageRating(List<Integer> ratings) {
        double totalRating = 0;
        int numbOfNotZeroRatings = 0;

        for(int rating:ratings){
            totalRating+=rating;
            if(rating>0){
                numbOfNotZeroRatings++;
            }
        }
        totalRating = totalRating / numbOfNotZeroRatings;
        return totalRating;
    }

    public List<CardRating> getAllCardRating() {
        List<CardRating> cardRatings = cardRatingRepository.findAll();
        return cardRatings;
    }

    public CardRating getCardRatingById(Long cardId) {
        CardRating cardRating = cardRatingRepository.findOne(cardId);
        return cardRating;
}

    public double countAvarageRating(double avarageRating, long numbOfUsersRatings, long rating) {
        double newAvarageRating=0;
        if(rating!=0)
            newAvarageRating = (avarageRating * numbOfUsersRatings + rating) / numbOfUsersRatings + 1;

        return numbOfUsersRatings;
    }

    public long countNumbOfUsersRatings(Long cardId) {
        Long numbOfUsersRatings = cardRatingRepository.countAllByCardId(cardId);
        return numbOfUsersRatings;
    }
}
