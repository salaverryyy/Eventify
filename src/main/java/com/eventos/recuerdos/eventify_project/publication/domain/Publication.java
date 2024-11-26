package com.eventos.recuerdos.eventify_project.publication.domain;

import com.eventos.recuerdos.eventify_project.comment.domain.Comment;
import com.eventos.recuerdos.eventify_project.like.domain.PublicationLike;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;  // Identificador único de la publicación

    // Relación Many-to-One con el Usuario (autor de la publicación)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)  // Cambiado para coincidir con 'author'
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount author;  // Usuario que hizo la publicación

    // Relación Many-to-One con Memory (recuerdo)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "memory_id", nullable = false)
    private Memory memory;  // Recuerdo al que pertenece la publicación

    // Tipo de archivo (FOTO o VIDEO)
    @Enumerated(EnumType.STRING)
    private FileType fileType;  // Tipo de archivo (foto o video)

    @Column(nullable = false)
    private String fileUrl;  // URL del archivo almacenado (en AWS S3)

    private String description;  // Descripción de la publicación

    @Column(nullable = false)
    private LocalDateTime publicationDate;  // Fecha y hora de la publicación

    @Column(name = "like_count", nullable = false, columnDefinition = "integer default 0")
    private int likeCount = 0;  // Contador de likes

    // Relación One-to-Many con Likes
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PublicationLike> publicationLikes = new ArrayList<>();

    // Relación One-to-Many con Comentarios
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


}
