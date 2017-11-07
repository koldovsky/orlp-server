package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

    List<Comment> findCourseCommentsByCourseId(Long courseId);

    @Modifying
    @Query(value = "DELETE FROM CourseComment c WHERE c.id =:commentId OR c.parentCommentId = :commentId")
    void deleteComment(@Param("commentId") Long commentId);
}
