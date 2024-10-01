package com.eventos.recuerdos.eventify_project.comment.infrastructure;

import com.eventos.recuerdos.eventify_project.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
