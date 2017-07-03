package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by askol on 6/30/2017.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Category findById(Long id);
	 List<Category> findTop4ByOrderById();
}
