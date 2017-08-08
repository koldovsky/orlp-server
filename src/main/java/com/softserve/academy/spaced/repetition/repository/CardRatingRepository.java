package com.softserve.academy.spaced.repetition.repository;


import com.softserve.academy.spaced.repetition.domain.CardRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRatingRepository extends JpaRepository<CardRating, Long> {

    @Query("select c.rating from CardRating c where c.cardId=:cardId")
    public List<Integer> findRatingByCardId(@Param("cardId") long cardId);

    @Query("select c.rating from CardRating c where c.deckId=:deckId")
    public List<Integer> findRatingByDeckId(@Param("deckId") long deckId);

    public Long countAllByRating(int rating);

    public List<CardRating> findAllByDeckId(long deckId);

    public CardRating findCardRatingByAccountEmailAndCardIdAndDeckId(String accountEmail,long cardId,long deckId);

    public Long countAllByCardId(Long id);
}
