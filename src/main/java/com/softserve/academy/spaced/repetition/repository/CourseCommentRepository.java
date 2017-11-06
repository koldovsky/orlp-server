package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.DTO.impl.CommentDTO;
import com.softserve.academy.spaced.repetition.domain.Card;
import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.Course;
import com.softserve.academy.spaced.repetition.domain.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

        List<Comment> findCourseCommentsByCourseId (Long courseId);

        List<Comment>  findCourseCommentsByParentCommentId(Long parentCommentId );
}
