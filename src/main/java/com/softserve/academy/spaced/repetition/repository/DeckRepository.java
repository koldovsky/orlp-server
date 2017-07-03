package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by askol on 6/30/2017.
 */
@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    @Query("select d from Deck d order by deck_rating desc")
    List<Deck> findTop4DecksOrderByDeckRating();

    List<Deck> getAllDecksByCategoryId(Long id);
}
