package com.eventos.recuerdos.eventify_project.comment.domain;

import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Identificador único del comentario

    // Relación Many-to-One con el Usuario que hizo el comentario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Usuario que realizó el comentario

    // Relación Many-to-One con la Publicación a la que pertenece el comentario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication; // Publicación a la que pertenece el comentario

    private String content; // Contenido del comentario

    private LocalDateTime commentDate; // Fecha y hora en que se hizo el comentario
}
