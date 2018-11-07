package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CoursePrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePriceRepository extends JpaRepository<CoursePrice, Long> {
}
