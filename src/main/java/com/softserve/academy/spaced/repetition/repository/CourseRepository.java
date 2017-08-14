package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> getAllCoursesByCategoryId(Long id);

    Course getCourseByCategoryIdAndId(Long category_id, Long course_id);

    List<Course> findTop4ByOrderById();

    @Query(value = "SELECT c FROM Course c WHERE c.category.id = :category_id AND c.id = :course_id")
    List<Course> getAccessToCourse(@Param("category_id") Long category_id, @Param("course_id") Long course_id);

    @Query(value = "SELECT c FROM Course c where c.category.id = :category_id")
    List<Course> getAccessToCourse(@Param("category_id") Long category_id);

    List<Course> findAllByOrderByRatingDesc();

}
