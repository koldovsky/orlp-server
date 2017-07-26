package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findById(Long id);

    List<Category> findTop4ByOrderById();

    @Query(value = "SELECT c.name, c.description, c.imagebase64 FROM Category c WHERE c.id = :category_id")
    List<Category> hasAccessToCategory(@Param("category_id") Long category_id);
}
