package com.softserve.academy.spaced.repetition.service;

import com.softserve.academy.spaced.repetition.domain.DeckRating;
import com.softserve.academy.spaced.repetition.repository.DeckRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckRatingService {

    @Autowired
    private DeckRatingRepository deckRatingRepository;

    public DeckRating addDeckRating(DeckRating deckRating) {
        return deckRatingRepository.save(deckRating);
    }

}
