package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.PointsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
    @Query(value = "select sum(p.points) from PointsTransaction p  where p.userFrom.id = :idUserFrom")
    Integer getAllExpensesByUser(@Param("idUserFrom") Long idUserFrom);

    @Query(value = "select sum(p.points) from PointsTransaction p where p.userTo.id = :idUserTo")
    Integer getAllIncomeByUser(@Param("idUserTo") Long idUserTo);

    @Query(value = "select distinct p from PointsTransaction p where p.userFrom.id = :idUser or p.userTo.id = :idUser")
    List<PointsTransaction> findAllTransactionsByUserId(@Param("idUser") long idUser);

}
