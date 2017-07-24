package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query(value = "SELECT c FROM Deck d  INNER JOIN d.cards AS c WHERE "
            + "d.id = :deck_id and c.id = :card_id")
    List<Card> hasAccessToCard(@Param("deck_id") Long deck_id, @Param("card_id") Long card_id);
}
