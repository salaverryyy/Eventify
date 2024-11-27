package com.eventos.recuerdos.eventify_project.comment.infrastructure;

import com.eventos.recuerdos.eventify_project.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Método para encontrar comentarios por el ID de la publicación
    List<Comment> findByPublicationId(Long publicationId);
}
