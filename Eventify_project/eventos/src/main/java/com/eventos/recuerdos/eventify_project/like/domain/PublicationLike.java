package com.eventos.recuerdos.eventify_project.like.domain;

import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "publication  _like")
public class PublicationLike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Identificador único del like

    // Relación Many-to-One con el Usuario que dio el like
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Usuario que dio el like

    // Relación Many-to-One con la Publicación a la que se dio el like
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication; // Publicación a la que se dio el like

    private LocalDateTime likeDate; // Fecha y hora en que se dio el like
}
