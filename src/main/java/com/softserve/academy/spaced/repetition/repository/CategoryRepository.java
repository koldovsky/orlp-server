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

    List<Category> findTop8ByOrderById();

    @Query(value = "SELECT c.name, c.description, c.image FROM Category c WHERE c.id = :categoryId")
    List<Category> hasAccessToCategory(@Param("categoryId") Long categoryId);

    List<Category>  findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String name, String description);
}
