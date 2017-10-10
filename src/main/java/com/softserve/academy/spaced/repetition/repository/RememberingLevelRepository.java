package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.RememberingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RememberingLevelRepository extends JpaRepository<RememberingLevel, Long> {
}
