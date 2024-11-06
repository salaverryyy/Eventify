package com.eventos.recuerdos.eventify_project.memory.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
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

    @Column(nullable = false)
    private String memoryName; // Nombre del recuerdo (álbum virtual)

    private String description; // Descripción del recuerdo
    private LocalDateTime memoryCreationDate; // Fecha de creación del recuerdo

    @Column(unique = true, nullable = false, length = 8)
    private String accessCode;

    @Column(unique = true, nullable = false)
    private String albumLink;

    // Relación Many-to-One con User (usuario creador del recuerdo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    // Relación One-to-Many con publicaciones
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Publication> publications = new ArrayList<>();

    // Relación One-to-Many con invitaciones
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Invitation> invitations = new ArrayList<>();

    // Relación One-to-One con Event
    @OneToOne(mappedBy = "memory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Event event;
}
