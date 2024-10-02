package com.eventos.recuerdos.eventify_project.publication.domain;

import com.eventos.recuerdos.eventify_project.comment.domain.Comment;
import com.eventos.recuerdos.eventify_project.like.domain.Like;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Identificador único de la publicación

    // Relación Many-to-One con el Usuario que hizo la publicación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Usuario que hace la publicación

    // Relación Many-to-One con Memory (recuerdo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id", nullable = false)
    private Memory memory; // Recuerdo al que pertenece la publicación

    // Tipo de archivo (FOTO o VIDEO)
    @Enumerated(EnumType.STRING)
    private FileType fileType; // Tipo de archivo (foto o video)

    private String fileUrl; // URL del archivo almacenado (en AWS S3, por ejemplo)

    private String description; // Descripción de la publicación

    private LocalDateTime publicationDate; // Fecha y hora de la publicación

    // Relación One-to-Many con Likes
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>(); // Lista de likes recibidos

    // Relación One-to-Many con Comentarios
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // Lista de comentarios en la publicación
}
