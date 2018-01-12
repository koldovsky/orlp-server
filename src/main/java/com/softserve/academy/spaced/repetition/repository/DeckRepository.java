package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    List<Deck> getAllDecksByCategoryId(Long id);

    Deck getDeckById(Long id);

    Deck getDeckByCategoryIdAndId(Long categoryId, Long deckId);

    List<Deck> findTop4ByOrderById();

    List<Deck> findAllByOrderByRatingDesc();

    void deleteDeckById(Long id);

    @Query(value = "SELECT d.id, d.name, d.description, d.rating, d.category, d.deckOwner FROM Course c " +
            "INNER JOIN c.decks AS d WHERE c.id = :course_id and d.id = :deck_id")
    List<Deck> hasAccessToDeck(@Param("course_id") Long courseId, @Param("deck_id") Long deckId);

    @Query(value = "SELECT d FROM Category c INNER JOIN c.decks AS d WHERE c.id = :category_id AND d.id = :deck_id")
    List<Deck> hasAccessToDeckFromCategory(@Param("category_id") Long categoryId, @Param("deck_id") Long deckId);

    @Query(value = "SELECT d FROM Deck d WHERE d.category.id = :category_id")
    List<Deck> hasAccessToDeckFromCategory(@Param("category_id") Long categoryId);

    @Query(value = "SELECT d FROM Deck d INNER JOIN d.deckOwner u  WHERE d.id = :deck_id AND u.id = :user_id")
    Deck getDeckByItsIdAndOwnerOfDeck(@Param("deck_id") Long deckId, @Param("user_id") Long userId);

    List<Deck> findAllByDeckOwnerIdEquals(Long userId);

    Page<Deck> findAllByCategoryEquals(Category category, Pageable pageable);
}
