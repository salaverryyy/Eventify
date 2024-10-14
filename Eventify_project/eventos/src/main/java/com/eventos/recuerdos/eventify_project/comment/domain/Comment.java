package com.eventos.recuerdos.eventify_project.comment.domain;

import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Identificador único del comentario

    // Relación Many-to-One con User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    // Relación Many-to-One con Publication
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    // Validación para evitar comentarios vacíos
    @NotBlank(message = "El comentario no puede estar vacío")
    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDateTime commentDate = LocalDateTime.now();
}