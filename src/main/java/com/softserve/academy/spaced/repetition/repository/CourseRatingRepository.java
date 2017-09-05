package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {

    @Query("select avg(c.rating) from CourseRating c where c.courseId=:courseId group by c.courseId")
    Double findRatingByCourseId(@Param("courseId") long courseId);

    CourseRating findAllByAccountEmailAndCourseId(String accountEmail, long courseId);
}
