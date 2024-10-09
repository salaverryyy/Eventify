package com.eventos.recuerdos.eventify_project.memory.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String memoryName; // Nombre del recuerdo (álbum virtual)
    private String description; // Descripción del recuerdo
    private LocalDateTime memoryCreationDate; // Fecha de creación del recuerdo

    // Relación Many-to-One con User (usuario creador del recuerdo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Relación One-to-Many con publicaciones
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Publication> publications = new ArrayList<>(); // Lista de publicaciones asociadas al recuerdo

    // Relación One-to-Many con invitaciones
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invitation> invitations = new ArrayList<>(); // Lista de invitaciones del recuerdo

    // Relación One-to-One con Event
    @OneToOne(mappedBy = "memory", fetch = FetchType.LAZY)
    private Event event; // Relación One-to-One con Event
}
