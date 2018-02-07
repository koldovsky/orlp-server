package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {

    @Query("select avg(c.rating) from CourseRating c where c.course.id =:courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);

    CourseRating findAllByAccountEmailAndCourseId(String accountEmail, Long courseId);
}
