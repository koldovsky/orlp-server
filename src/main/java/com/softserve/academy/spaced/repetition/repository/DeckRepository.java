package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.DTO.DeckDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    /*@Query("select d from DeckDTO d order by deck_rating desc")
    List<Deck> findTop4DecksOrderByDeckRating();*/

    List<Deck> getAllDecksByCategoryId(Long id);

    List<Deck> findTop4ByOrderById();

}
