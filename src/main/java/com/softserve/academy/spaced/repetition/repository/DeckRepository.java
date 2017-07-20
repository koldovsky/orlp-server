package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Deck;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    List<Deck> getAllDecksByCategoryId(Long id);

    Deck getDeckByCategoryIdAndId(Long category_id, Long deck_id);

    List<Deck> findTop4ByOrderById();

    List<Deck> findAllByOrderByRatingDesc();

    @Query(value = "SELECT d.id, d.name, d.description, d.rating, d.category, d.deckOwner FROM Course c  INNER JOIN c.decks AS d WHERE "
            + "c.id = :course_id and d.id = :deck_id")
    List<Deck> hasAccessToDeck(@Param("course_id") Long course_id, @Param("deck_id") Long deck_id);

    @Query(value = "SELECT d FROM Deck d, Category c WHERE c.id = :category_id AND d.id = :deck_id")
    List<Deck> hasAccessToDeckFromCategory(@Param("category_id") Long category_id, @Param("deck_id") Long deck_id);

    @Query(value = "SELECT d FROM Deck d WHERE d.category.id = :category_id")
    List<Deck> hasAccessToDeckFromCategory(@Param("category_id") Long category_id);

}
