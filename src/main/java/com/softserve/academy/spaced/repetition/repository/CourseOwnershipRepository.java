package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CourseOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseOwnershipRepository extends JpaRepository<CourseOwnership,Long> {

    void deleteCourseOwnershipByCourseId(Long courseId);
}
