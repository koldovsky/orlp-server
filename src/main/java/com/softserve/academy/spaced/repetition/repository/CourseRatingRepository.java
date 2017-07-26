package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRatingRepository extends JpaRepository<CourseRating,Long> {

    @Query("select c.rating from CourseRating c where c.courseId=:courseId")
    public List<Integer> findRatingByCourseId(@Param("courseId") long courseId);

    public CourseRating findAllByAccountEmailAndCourseId(String accountEmail,long courseId);

    public Long countAllByCourseId(Long courseId);

}
