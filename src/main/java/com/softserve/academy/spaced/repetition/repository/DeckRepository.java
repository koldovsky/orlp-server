package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository <Deck, Long> {

    List <Deck> getAllDecksByCategoryId(Long id);

    Deck getDeckByCategoryIdAndId(Long category_id, Long deck_id);

    List<Deck> findTop4ByOrderById();
}
