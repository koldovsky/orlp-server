package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.DeckRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRatingRepository extends JpaRepository<DeckRating, Long> {



}
