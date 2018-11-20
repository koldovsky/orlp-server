package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
    @Query("select sum (c.points) from PointsTransaction c where c.userFrom.id =:userId")
    Integer getAllExpensesByUser(@Param("userId") Long userId);

    @Query("select sum (c.points) from PointsTransaction c where c.userTo.id =:userId")
    Integer getAllIncomeByUser(@Param("userId") Long userId);
}
