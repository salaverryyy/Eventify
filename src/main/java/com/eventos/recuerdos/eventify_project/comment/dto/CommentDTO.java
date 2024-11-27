package com.eventos.recuerdos.eventify_project.comment.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    @Column(updatable = false)
    private LocalDateTime commentDate;
    private Long publicationId;  // Relacionado a una publicación
    private Long userId;  // Usuario que hizo el comentario
}

//Transferir los comentarios realizados en publicaciones
