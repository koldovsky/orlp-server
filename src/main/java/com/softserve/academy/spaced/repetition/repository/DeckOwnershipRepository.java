package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.DeckOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckOwnershipRepository extends JpaRepository<DeckOwnership, Long> {
    void deleteDeckOwnershipByDeckId(Long deckId);
}
