package com.eventos.recuerdos.eventify_project.comment.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long publicationId;  // Relacionado a una publicación
    private Long userId;  // Usuario que hizo el comentario
}

//Transferir los comentarios realizados en publicaciones
