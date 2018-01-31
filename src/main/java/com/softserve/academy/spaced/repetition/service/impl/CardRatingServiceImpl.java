package com.softserve.academy.spaced.repetition.service.impl;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.CardRating;
import com.softserve.academy.spaced.repetition.domain.User;
import com.softserve.academy.spaced.repetition.utils.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.repository.CardRatingRepository;
import com.softserve.academy.spaced.repetition.repository.CardRepository;
import com.softserve.academy.spaced.repetition.repository.DeckRepository;
import com.softserve.academy.spaced.repetition.service.CardRatingService;
import com.softserve.academy.spaced.repetition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardRatingServiceImpl implements CardRatingService {

    @Autowired
    private CardRatingRepository cardRatingRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserService userService;

    @Override
    public void addCardRating(CardRating cardRating, Long cardId) throws NotAuthorisedUserException {
        User user = userService.getAuthorizedUser();
        String email = user.getAccount().getEmail();
        CardRating cardRatingByAccountEmail = cardRatingRepository
                .findCardRatingByAccountEmailAndCard_Id(email, cardId);
        if (cardRatingByAccountEmail != null) {
            cardRating.setId(cardRatingByAccountEmail.getId());
        }
        Card card = cardRepository.findOne(cardId);
        cardRating.setAccountEmail(email);
        cardRating.setCard(card);
        cardRatingRepository.save(cardRating);
        double cardAverageRating = cardRatingRepository.findRatingByCard_Id(cardId);
        card.setRating(cardAverageRating);
        cardRepository.save(card);
    }

    @Override
    public CardRating getCardRatingById(Long cardId) {
        return cardRatingRepository.findOne(cardId);
    }
}
