package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.DeckPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckPriceRepository extends JpaRepository<DeckPrice, Long> {
}
