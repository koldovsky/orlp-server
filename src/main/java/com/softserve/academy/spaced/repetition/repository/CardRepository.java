package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> getAllCardsByDeckId(Long id);
    //void save(Long id, Card card);
}
