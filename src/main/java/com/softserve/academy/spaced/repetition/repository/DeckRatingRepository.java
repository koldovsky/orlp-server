package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.DeckRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeckRatingRepository extends JpaRepository<DeckRating, Long> {

    @Query("select avg(d.rating) from DeckRating d where d.deck.id =:deckId group by d.deck.id")
    Double findRatingByDeckId(@Param("deckId") long deckId);

    DeckRating findAllByAccountEmailAndDeckId(String accountEmail, long deckId);
}
