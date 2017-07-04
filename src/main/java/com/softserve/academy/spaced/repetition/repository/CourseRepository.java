package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by askol on 6/30/2017.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> getAllCoursesByCategoryId(Long id);
}
