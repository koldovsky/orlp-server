package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> getAllCoursesByCategoryIdAndPublishedTrue(Long id);

    Course getCourseByCategoryIdAndId(Long categoryId, Long courseId);

    Course getCourseById(Long courseId);

    List<Course> findTop4ByOrderByRating();

    @Query(value = "SELECT c FROM Course c WHERE c.category.id = :category_id AND c.id = :course_id")
    List<Course> getAccessToCourse(@Param("category_id") Long categoryId, @Param("course_id") Long courseId);

    @Query(value = "SELECT c FROM Course c where c.category.id = :category_id")
    List<Course> getAccessToCourse(@Param("category_id") Long categoryId);

    List<Course> findAllByPublishedTrueOrderByRatingDesc();

    List<Course> findAllByPublishedTrue();

    Page<Course> findAllByCategoryEqualsAndPublishedTrue(Category category, Pageable pageable);

    Page<Course> findAllByPublishedTrue(Pageable pageable);

    @Query(value = "SELECT c.course_id FROM course c WHERE c.name LIKE %:searchString% OR c.description LIKE %:searchString%",
            nativeQuery = true)
    Set<BigInteger> findCoursesId(@Param("searchString") String searchString);

    List<Course> findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String name, String description);

    @Modifying
    @Query(value = "DELETE FROM user_courses WHERE course_id = :courseId", nativeQuery = true)
    void deleteSubscriptions(@Param("courseId") Long courseId);
}
