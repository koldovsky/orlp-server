package com.softserve.academy.spaced.repetition.repository;

import com.softserve.academy.spaced.repetition.domain.Comment;
import com.softserve.academy.spaced.repetition.domain.DeckComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckCommentRepository extends JpaRepository<DeckComment, Long> {

    List<Comment> findDeckCommentsByDeckId(Long deckId);

    @Modifying
    @Query(value = "DELETE FROM CourseComment c WHERE c.id =:commentId OR c.parentCommentId = :commentId")
    void deleteComment(@Param("commentId") Long commentId);
}

