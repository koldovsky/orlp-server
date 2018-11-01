package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    Deck getDeckById(Long id);

    List<Deck> findAllByOrderByRatingDesc();

    @Query(value = "SELECT d FROM Deck d INNER JOIN d.deckOwner u  WHERE d.id = :deck_id AND u.id = :user_id")
    Deck getDeckByItsIdAndOwnerOfDeck(@Param("deck_id") Long deckId, @Param("user_id") Long userId);

    List<Deck> findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String name, String description);

    List<Deck> findAllByDeckOwnerIdEquals(Long userId);

    Page<Deck> findAllByCategoryEquals(Category category, Pageable pageable);

    @Query(value = "SELECT d.deck_id FROM deck d WHERE d.name LIKE %:searchString% or d.description LIKE %:searchString%",
            nativeQuery = true)
    Set<BigInteger> findDecksId(@Param("searchString") String searchString);

    Page<Deck> findAllByCategoryEqualsAndHiddenFalse(Category category, Pageable pageable);

    List<Deck> findAllByHiddenFalseOrderByRatingDesc();

}
