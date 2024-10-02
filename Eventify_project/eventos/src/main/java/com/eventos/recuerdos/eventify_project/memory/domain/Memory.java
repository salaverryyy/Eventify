package com.eventos.recuerdos.eventify_project.memory.domain;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relación One-to-Many con publicaciones
    @OneToMany(mappedBy = "memory", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Publication> publications = new ArrayList<>(); // Lista de publicaciones asociadas al recuerdo

    // Relación Many-to-Many con eventos
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "memory_event",
            joinColumns = @JoinColumn(name = "memory_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>(); // Lista de eventos asociados al recuerdo
}
