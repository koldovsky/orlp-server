package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
}
