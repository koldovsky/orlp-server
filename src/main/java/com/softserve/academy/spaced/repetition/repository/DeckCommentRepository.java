package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.DeckComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckCommentRepository extends JpaRepository<DeckComment, Long> {

    List<DeckComment> findDeckCommentsByDeckId(Long deckId);
}

