package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CourseOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseOwnershipRepository extends JpaRepository<CourseOwnership,Long> {
}
